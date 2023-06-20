package in.westerncoal.biometric.job;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.app.Device;
import in.westerncoal.biometric.enums.PullStatus;
import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPullLogKey;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.Terminal.TerminalBuilder;
import in.westerncoal.biometric.server.operation.GetAllLog;
import in.westerncoal.biometric.service.AttendanceService;
import in.westerncoal.biometric.service.ServerPullLogService;
import in.westerncoal.biometric.service.ServerPullService;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.types.DeviceStatus;
import in.westerncoal.biometric.types.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BiometricDataPullScheduler {
	@Autowired
	BiometricDevicePool biometricDevicePool;

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

	@Scheduled(fixedDelay = 5000, initialDelay = 0)
	public void pullData() {
		WebSocket ws = null;
		DeviceStatus deviceStatus;
		ServerPull serverPull = null;
		String getAllLogJson = null;
		Set<ServerPullLog> serverPullLogs = new TreeSet<ServerPullLog>();
		ServerPullLogKey serverPullLogKey = null;
		ServerPullLog serverPullLog = null;

		boolean firstTime = true;
		for (String sn : biometricDevicePool.keySet()) {

			ws = biometricDevicePool.get(sn).getWebSocket();
			deviceStatus = biometricDevicePool.get(sn).getDeviceStatus();
			if (deviceStatus == DeviceStatus.DEVICE_ACTIVE && !ws.isClosed()) {
				try {
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
					getAllLogJson = BioUtil.getObjectMapper().writeValueAsString(getAllLog);

					if (firstTime) {
						serverPull = ServerPull.builder().serverId(ws.getRemoteSocketAddress().toString().substring(1))
								.pullCommand(getAllLogJson).pullType(terminalDataPullMode).build();
						serverPullService.saveServerPull(serverPull);
						firstTime = false;
					}
					Terminal terminal = Terminal.builder().terminalId(sn).build();
					serverPullLogKey = ServerPullLogKey.builder().pullId(serverPull.getPullId()).terminalId(sn).build();
					serverPullLog = ServerPullLog.builder().pullStatus(PullStatus.IN_PROGRESS).serverPullLogKey(serverPullLogKey).serverPull(serverPull).terminal(terminal).build();
					ws.send(getAllLogJson);
					serverPullLogService.saveServerPullLog(serverPullLog);
					log.info("{}[{}] <- {}{}", sn, ws.getRemoteSocketAddress(), MessageType.DEVICE_GETALLLOG_MSG,
							getAllLogJson);
				} catch (JsonProcessingException | ParseException e) {
					log.error("Parsing Error {}", e);
				} catch (WebsocketNotConnectedException e2) {
					terminalService.setTerminalInactive(sn);
					ServerPullLog.builder().pullStatus(PullStatus.ERROR);
					serverPullLogService.saveServerPullLog(serverPullLog);
					biometricDevicePool.setDeviceInactive(ws);
				}

			} else {
				terminalService.setTerminalInactive(sn);
				biometricDevicePool.getDevice(sn).setDeviceStatus(DeviceStatus.DEVICE_INACTIVE);
			}
		}
		 
	}

}
