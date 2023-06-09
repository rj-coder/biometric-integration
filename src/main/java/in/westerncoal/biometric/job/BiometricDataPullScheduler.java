package in.westerncoal.biometric.job;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.app.Device;
import in.westerncoal.biometric.server.operation.GetAllLog;
import in.westerncoal.biometric.types.DeviceStatus;
import in.westerncoal.biometric.types.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BiometricDataPullScheduler {
	@Autowired
	BiometricDevicePool biometricDevicePool;

	@Value("${terminal.data.pull.mode}")
	private char terminalDataPullMode;

 	//@Scheduled(fixedDelay = 20000, initialDelay = 0)
	public void pullData() {
		WebSocket ws;
		DeviceStatus deviceStatus;
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
					String getAllLogJson = BioUtil.getObjectMapper().writeValueAsString(getAllLog);
					ws.send(getAllLogJson);
					log.info("{}[{}] <- {}{}", 
							sn,ws.getRemoteSocketAddress(),MessageType.DEVICE_GETALLLOG_MSG,getAllLogJson );
				} catch (JsonProcessingException | ParseException e) {
					log.error("Parsing Error {}", e);
				} catch (WebsocketNotConnectedException e2) {
					biometricDevicePool.getDevice(sn).setDeviceStatus(DeviceStatus.DEVICE_INACTIVE);
				}

			} else {
				biometricDevicePool.getDevice(sn).setDeviceStatus(DeviceStatus.DEVICE_INACTIVE);
			}
		}

	}

}
