package in.westerncoal.biometric.job;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.enums.OperationType;
import in.westerncoal.biometric.enums.TerminalOperationStatus;
import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPullLogKey;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.server.operation.GetAllLog;
import in.westerncoal.biometric.service.AttendanceService;
import in.westerncoal.biometric.service.ServerPullLogService;
import in.westerncoal.biometric.service.ServerPullService;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.util.BioUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BiometricDataPullScheduler {

	@Autowired
	AttendanceService attendanceService;

	@Autowired
	TerminalService terminalService;

	@Autowired
	ServerPullService serverPullService;

	@Autowired
	ServerPullLogService serverPullLogService;

	@Value("${terminal.data.pull.mode}")
	private char terminalDataPullMode;

	@Scheduled(fixedDelay = 12000, initialDelay = 0)
	@Transactional
	public void pullData() throws ParseException, UnknownHostException, JsonProcessingException {

		List<ServerPullLog> serverPullLogs = new ArrayList<>();

		boolean isPullHasActiveTerminals = false;
		boolean createServerPull = true;
		for (Terminal terminal : TerminalOperationCache.getActiveTerminals()) {
			if (!TerminalOperationCache.getTerminalOperationLog(terminal).getTerminalOperationStatus()
					.equals(TerminalOperationStatus.IN_PROGRESS) && terminal.getLock().tryLock()) {
				ServerPull serverPull = null;
				if (createServerPull) {
					// Create ServerPull & Save in DB
					serverPull = ServerPull.builder().serverId(InetAddress.getLocalHost().toString()).build();
					serverPullService.saveServerPull(serverPull);
					createServerPull = false;
				}
				ServerPullLogKey serverPullLogKey = ServerPullLogKey.builder().pullId(serverPull.getPullId())
						.terminalId(terminal.getTerminalId()).build();

				ServerPullLog serverPullLog = ServerPullLog.builder().serverPullLogKey(serverPullLogKey).build();
				serverPullLogs.add(serverPullLog);
				isPullHasActiveTerminals = true;
				terminal.getLock().unlock();
			}
		}

		if (isPullHasActiveTerminals) {

			for (ServerPullLog serverPullLog : serverPullLogs) {
				GetAllLog getAllLog = createGetAllLogCriteria(serverPullLog);

				String getAllLogJson = BioUtil.getObjectMapper().writeValueAsString(getAllLog);
				serverPullLog.setPullCommand(getAllLogJson);

				Terminal terminal = TerminalOperationCache
						.getTerminal(serverPullLog.getServerPullLogKey().getTerminalId());
				TerminalOperationLog terminalOperationLog = TerminalOperationLog.builder()
						.operationType(OperationType.DEVICE_GETALLLOG_OPERATION_SERVER).recordCount(0).recordFetched(0)
						.recordFetchOperation(true).pullId(serverPullLog.getServerPullLogKey().getPullId())
						.terminalId(terminal.getTerminalId()).terminal(terminal).build();
				if (serverPullLog.getLock().tryLock())
					try {
						terminalService.doExecute(terminalOperationLog, serverPullLog.getPullCommand());
					} finally {
						serverPullLog.getLock().unlock();
					}
				else
					log.warn("Server Pull Skipped for {}", terminal.getTerminalId());
			}
		}

	}

	private GetAllLog createGetAllLogCriteria(ServerPullLog serverPullLog) throws ParseException {
		Date fromDate = null, toDate = null;
		GetAllLog getAllLog = new GetAllLog();
		switch (terminalDataPullMode) {
		case 'A':
			break;
		case 'C':
			fromDate = BioUtil.getDateFormatter().parse(LocalDate.now().withDayOfMonth(1).toString());
			toDate = BioUtil.getDateFormatter().parse(LocalDate.now().toString());
			break;
		case 'P':
			fromDate = BioUtil.getDateFormatter().parse(LocalDate.now().minusMonths(1).toString());
			toDate = BioUtil.getDateFormatter().parse(LocalDate.now().toString());
			break;
		case 'X':
			break;
		default:
			break;
		}
		getAllLog.setFrom(fromDate);
		getAllLog.setTo(toDate);
		return getAllLog;
	}

}
