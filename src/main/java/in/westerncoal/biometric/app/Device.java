package in.westerncoal.biometric.app;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.client.operation.GetAllLogReply;
import in.westerncoal.biometric.job.BiometricDevicePool;
import in.westerncoal.biometric.server.operation.DeviceRegisterReply;
import in.westerncoal.biometric.server.operation.GetAllLogReplyServerResponse;
import in.westerncoal.biometric.server.operation.SendLogReply;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.types.DeviceStatus;
import in.westerncoal.biometric.types.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@Setter
@AllArgsConstructor
public class Device {
	private String serialNo;
	private WebSocket webSocket;
	private DeviceStatus deviceStatus;
	private DeviceOperation deviceOperation;
	private boolean isDeviceOperationCompleted;

 
	
	public void doDeviceRegisterReply(DeviceRegisterReply deviceRegisterReply,
			BiometricDevicePool biometricDevicePool) {
		String reply;
		try {
			reply = BioUtil.getObjectMapper().writeValueAsString(deviceRegisterReply);
			this.webSocket.send(reply);
			biometricDevicePool.getDevice(serialNo).setDeviceOperationCompleted(true);
			log.info("{}[{}] <- {}{}", serialNo, webSocket.getRemoteSocketAddress(),
					MessageType.DEVICE_INIT_REGISTER_REPLY_MSG, reply);
		} catch (WebsocketNotConnectedException e) {
			biometricDevicePool.removeDevice(serialNo);
		} catch (JsonProcessingException e) {
			log.error("JSON Parsing Exception {}", e);
		}

	}

	public void doSendLogReply(SendLogReply sendLogReply, WebSocket conn, BiometricDevicePool biometricDevicePool) {
		try {

			String reply = BioUtil.getObjectMapper().writeValueAsString(sendLogReply);
			conn.send(reply);
			biometricDevicePool.getDevice(serialNo).setDeviceOperationCompleted(true);
			log.info("{}[{}] <- {}{}", serialNo, webSocket.getRemoteSocketAddress(), conn.getLocalSocketAddress(),
					reply);
		} catch (WebsocketNotConnectedException e) {
			biometricDevicePool.get(serialNo).setDeviceStatus(DeviceStatus.DEVICE_INACTIVE);
		} catch (JsonProcessingException e) {
			log.error("JSON Parsing Exception {}", e);
		}
	}

	public void doGetAllLogReplyServerResponse(GetAllLogReply getAllLogReply, WebSocket conn,
			BiometricDevicePool biometricDevicePool) {
		try {
			GetAllLogReplyServerResponse getAllLogReplyServerResponse = new GetAllLogReplyServerResponse();
			String reply = BioUtil.getObjectMapper().writeValueAsString(getAllLogReplyServerResponse);
			conn.send(reply);
			biometricDevicePool.getDevice(serialNo).setDeviceOperationCompleted(true);
			log.info("{}[{}] <- {}{}", serialNo, conn.getRemoteSocketAddress(),
					MessageType.DEVICE_GETALLLOG_REPLY_RESPONSE_MSG, reply);
		} catch (WebsocketNotConnectedException e) {
		} catch (JsonProcessingException e) {
			log.error("JSON Parsing Exception {}", e);

		}

	}

	 

}
