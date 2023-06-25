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

	private static List<ServerPullLog> serverPullLogs = new ArrayList<>();

	public static ServerPullLog getServerPullLogByTerminal(Terminal terminal) {
		return serverPullLogs.stream()
				.filter(t -> t.getServerPullLogKey().getTerminalId().compareTo(terminal.getTerminalId()) == 0)
				.findFirst().orElse(null);
	}

	@Scheduled(cron = "0 0 3,9,11,15,19,23 * * ?",fixedDelay = 60000,initialDelay = 60000)
	public void pullData() throws ParseException, UnknownHostException, JsonProcessingException {

		// Await if previous pull is in progress
		for (ServerPullLog s : serverPullLogs)
			try {
				s.getPullLogLatch().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		serverPullLogs.clear();

		boolean isPullHasActiveTerminals = false;
		boolean createServerPull = true;
		ServerPull serverPull = null;
		for (Terminal terminal : TerminalOperationCache.getActiveTerminals()) {
			if (!TerminalOperationCache.getTerminalOperationLog(terminal).getTerminalOperationStatus()
					.equals(TerminalOperationStatus.IN_PROGRESS) && terminal.getLock().tryLock()) {

				if (createServerPull) {
					// Create ServerPull & Save in DB
					serverPull = ServerPull.builder().serverId(InetAddress.getLocalHost().toString()).build();
					serverPull = serverPullService.saveServerPull(serverPull);
					createServerPull = false;
				}
				ServerPullLogKey serverPullLogKey = ServerPullLogKey.builder().pullId(serverPull.getPullId())
						.terminalId(terminal.getTerminalId()).build();

				ServerPullLog serverPullLog = ServerPullLog.builder().serverPullLogKey(serverPullLogKey)
						.pullType(terminalDataPullMode)
						.build();
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
						.operationType(OperationType.DEVICE_GETALLLOG_OPERATION).recordCount(0).recordFetched(0)
						.recordFetchOperation(true).pullId(serverPullLog.getServerPullLogKey().getPullId())
						.terminalId(terminal.getTerminalId()).terminal(terminal).build();

				terminalService.doExecute(terminalOperationLog, serverPullLog);

			}
		}

	}

	private GetAllLog createGetAllLogCriteria(ServerPullLog serverPullLog) throws ParseException {
		Date fromDate = null, toDate = BioUtil.getDateFormatter().parse(LocalDate.now().toString());

		GetAllLog getAllLog = new GetAllLog();
		switch (terminalDataPullMode) {
		case 'A':
			break;
		case 'D': // Dynamic from DB
			fromDate = attendanceService
					.findMaxDateFromBiometricMachine(serverPullLog.getServerPullLogKey().getTerminalId());
			break;
		case 'C': // Always 1 to current date
			fromDate = BioUtil.getDateFormatter().parse(LocalDate.now().withDayOfMonth(1).toString());
			break;
		case 'P': // Always Previous Day + current day
			fromDate = BioUtil.getDateFormatter().parse(LocalDate.now().minusDays(1).toString());
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
