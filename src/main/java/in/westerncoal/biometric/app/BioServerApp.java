package in.westerncoal.biometric.app;

import java.net.InetSocketAddress;
import java.util.Calendar;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.westerncoal.biometric.client.operation.DeviceRegister;
import in.westerncoal.biometric.client.operation.GetAllLogReply;
import in.westerncoal.biometric.client.operation.SendLog;
import in.westerncoal.biometric.job.BiometricDevicePool;
import in.westerncoal.biometric.server.operation.DeviceRegisterReply;
import in.westerncoal.biometric.server.operation.SendLogReply;
import in.westerncoal.biometric.types.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BioServerApp extends WebSocketServer {

	@Autowired
	private BiometricDevicePool biometricDevicePool;

	private ObjectMapper objectMapper;

	public BioServerApp(@Value("${websocket.port}") int WS_PORT) {
		super(new InetSocketAddress(WS_PORT));
		objectMapper = BioUtil.getObjectMapper();
		this.start();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		log.info("Device Connected: {}", conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		biometricDevicePool.removeDevice(conn);
		log.info("Device Closed: {}", conn.getRemoteSocketAddress());

	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		MessageType msgType = Message.getMessageType(message);
		log.debug("{} - {} from {}", msgType, message, conn.getRemoteSocketAddress());

		switch (msgType) {
		case DEVICE_INIT_REGISTER_MSG: {
			deviceRegister(conn, message);
			break;
		}
		case DEVICE_SENDLOG_MSG: {
			sendLog(conn, message);
			break;
		}
		case DEVICE_SENDUSER_MSG:
			break;
		case DEVICE_GETALLLOG_REPLY_MSG: {
			getAllLogReply(conn, message);
			break;
		}
		case UNKNOWN_MSG: {
			break;
		}

		default:
			break;
		}
	}

	private void getAllLogReply(WebSocket conn, String message) {
		try {
			GetAllLogReply getAllLogReply = objectMapper.readValue(message, GetAllLogReply.class);
			log.info("{}[{}] -> {}{}", getAllLogReply.getSn(), conn.getRemoteSocketAddress(),
					MessageType.DEVICE_GETALLLOG_REPLY_MSG, message);
			Device device = getAllLogReply.updateDevice(getAllLogReply.getSn(), conn, biometricDevicePool,
					getAllLogReply, false);
			if (getAllLogReply.isEmptyReply())
				log.warn("{}[{}] -> {} completed", device.getSerialNo(), conn.getRemoteSocketAddress(),
						MessageType.DEVICE_GETALLLOG_MSG);
			else
				device.doGetAllLogReplyServerResponse(getAllLogReply, conn, biometricDevicePool);
		} catch (JsonMappingException e) {

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void sendLog(WebSocket conn, String message) {
		try {
			SendLog sendLog = objectMapper.readValue(message, SendLog.class);
			SendLogReply sendLogReply = new SendLogReply(true, sendLog.getCount(), sendLog.getLogindex(),
					BioUtil.getDateTimeFormatter().format(Calendar.getInstance().getTime()), 1);
			Device device = sendLog.updateDevice(sendLog.getSn(), conn, biometricDevicePool, sendLogReply, false);
			log.info("{}[{}] -> {}{}", device.getSerialNo(), conn.getRemoteSocketAddress(),
					MessageType.DEVICE_SENDLOG_MSG, message);
			device.doSendLogReply(sendLogReply, conn, biometricDevicePool);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.error("Invalid JSON Data - {}  from terminal {}", message, conn.getRemoteSocketAddress());
		}
	}

	private void deviceRegister(WebSocket conn, String message) {
		try {
			DeviceRegister deviceRegister = objectMapper.readValue(message, DeviceRegister.class);
			log.info("{}[{}] -> {}{}",  deviceRegister.getSn(),
					conn.getRemoteSocketAddress(),MessageType.DEVICE_INIT_REGISTER_MSG,message);
			DeviceRegisterReply deviceRegisterReply = new DeviceRegisterReply(
					BioUtil.getDateTimeFormatter().format(Calendar.getInstance().getTime()));
			Device device = deviceRegister.addDevice(deviceRegister.getSn(), conn, biometricDevicePool,
					deviceRegisterReply, false);
			device.doDeviceRegisterReply(deviceRegisterReply, biometricDevicePool);
			log.info("{}[{}] <-> {}", device.getSerialNo(), conn.getRemoteSocketAddress(),
					conn.getLocalSocketAddress());
		} catch (JsonProcessingException e3) {
			log.error("Invalid JSON Data from terminal {}", message, conn.getRemoteSocketAddress());
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if (biometricDevicePool != null)
			biometricDevicePool.removeDevice(conn);
		log.error("Server communication error {} with exception: {}", conn, ex);
	}

	@Override
	public void onStart() {
		log.info("Biometric Integration Websocket Server Started");
	}
}
