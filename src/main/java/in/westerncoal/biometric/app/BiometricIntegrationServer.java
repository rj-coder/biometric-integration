package in.westerncoal.biometric.app;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.westerncoal.biometric.client.operation.TerminalRegister;
import in.westerncoal.biometric.client.operation.GetAllLogReply;
import in.westerncoal.biometric.client.operation.SendLog;
import in.westerncoal.biometric.enums.MessageType;
import in.westerncoal.biometric.enums.OperationType;
import in.westerncoal.biometric.enums.TerminalOperationStatus;
import in.westerncoal.biometric.enums.TerminalStatus;
import in.westerncoal.biometric.job.BiometricDataPullScheduler;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPullLogKey;
import in.westerncoal.biometric.model.TerminalSend;
import in.westerncoal.biometric.server.operation.TerminalRegisterReply;
import in.westerncoal.biometric.server.operation.GetAllLogReplyServerResponse;
import in.westerncoal.biometric.server.operation.SendLogReply;
import in.westerncoal.biometric.service.AttendanceService;
import in.westerncoal.biometric.service.ServerPullService;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.service.TerminalSendLogService;
import in.westerncoal.biometric.util.BioUtil;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration

public class BiometricIntegrationServer extends WebSocketServer {

	
	@Autowired
	TerminalService terminalService;
	
	
	@Autowired
	AttendanceService attendanceService;


	@Autowired
	ServerPullService serverPullService;

	@Autowired
	TerminalSendLogService terminalSendLogService;

	private ObjectMapper objectMapper;

	public BiometricIntegrationServer(@Value("${websocket.port}") int WS_PORT) {
		super(new InetSocketAddress(WS_PORT));
		objectMapper = BioUtil.getObjectMapper();
		this.start();
	}

	
	@PreDestroy
	public void onShutdown() {
		log.info("Biometric Integration Server shutdown IN PROGRESS, releasing,clean up of resources in progress ");
		try {
			this.stop(10);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
		// Retrieve the last terminal operation
		for (Terminal terminal : TerminalOperationCache.getActiveTerminals()) {
			TerminalOperationLog terminalOperationLog = TerminalOperationCache.getTerminalOperationLog(terminal);
			if (terminalOperationLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS)) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);
				terminalService.save(terminalOperationLog);
				ServerPullLog serverPullLog = BiometricDataPullScheduler.getServerPullLogByTerminal(terminal);
				if (serverPullLog != null)
					serverPullLog.getPullLogLatch().countDown();// down latch
			} else
				continue;
		}
		log.info("Biometric Integration Server shut down COMPLETED");
	}
	

	@Override
	public void onStart() {
		log.info("Biometric Integration Websocket Server Started");
	}

	@Override
	public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
		log.info("Terminal Connected: {}", webSocket.getRemoteSocketAddress().toString().substring(1));
	}

	@Override
	public void onError(WebSocket webSocket, Exception ex) {
		Terminal terminal = TerminalOperationCache.getTerminalByWebSocket(webSocket);
		if (terminal != null) {
			terminal.getLock().lock();
			try {
				TerminalOperationLog terminalOperationLog = TerminalOperationCache.getTerminalOperationLog(terminal);
				terminal.setTerminalStatus(TerminalStatus.INACTIVE);
				terminalOperationLog.setTerminal(terminal);
				if (terminalOperationLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS)) {
					if (terminalOperationLog.getRecordCount() == terminalOperationLog.getRecordFetched()
							&& terminalOperationLog.getRecordCount() != 0) {
						terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);

					} else
						terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
					terminalService.updateTerminalOperationLog(terminalOperationLog);
					terminalOperationLog.setTerminal(terminal);
				}
				terminalService.updateTerminal(terminal);
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);
				ServerPullLog serverPullLog = BiometricDataPullScheduler.getServerPullLogByTerminal(terminal);
				if (serverPullLog != null)
					serverPullLog.getPullLogLatch().countDown();// down latch
				log.info("{}[{}} -E- {} : WebSocket Error", terminal.getTerminalId(),
						webSocket.getRemoteSocketAddress(), webSocket.getLocalSocketAddress());
			} finally {
				terminal.getLock().unlock();
			}
		} else
			log.error("Server communication error {} with exception: {}", webSocket, ex);
	}

	@Override
	public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
		Terminal terminal = TerminalOperationCache.getTerminalByWebSocket(webSocket);
		if (terminal != null) {
			terminal.getLock().lock();
			try {
				TerminalOperationLog terminalOperationLog = TerminalOperationCache.getTerminalOperationLog(terminal);
				terminal.setTerminalStatus(TerminalStatus.INACTIVE);
				terminalOperationLog.setTerminal(terminal);
				if (terminalOperationLog.getRecordCount() == terminalOperationLog.getRecordFetched()
						&& terminalOperationLog.getRecordCount() != 0) {
					if (terminalOperationLog.getRecordCount() == terminalOperationLog.getRecordFetched()
							&& terminalOperationLog.getRecordCount() != 0)
						terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);
					else
						terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
					terminalService.save(terminalOperationLog);
				}
				terminalService.updateTerminal(terminal);
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);
				ServerPullLog serverPullLog = BiometricDataPullScheduler.getServerPullLogByTerminal(terminal);
				if (serverPullLog != null)
					serverPullLog.getPullLogLatch().countDown();// down latch
				log.info("{}[{}} >-< {}", terminal.getTerminalId(), webSocket.getRemoteSocketAddress(),
						webSocket.getLocalSocketAddress());
			} finally {
				terminal.getLock().unlock();
			}
		} else {
			log.info("Terminal Closed: {}", webSocket.getRemoteSocketAddress().getAddress());
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		MessageType msgType = Message.getMessageType(conn, message);
		log.debug("{} - {} from {}", msgType, message, conn.getRemoteSocketAddress());

		switch (msgType) {
		case DEVICE_INIT_REGISTER_MSG: {

			try {
				deviceRegister(conn, message);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			break;
		}
		case DEVICE_SENDLOG_MSG: {
			// sendLog(conn, message);
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

	private void deviceRegister(WebSocket websocket, String message) throws UnknownHostException {
		try {
			TerminalRegister terminalRegister = objectMapper.readValue(message, TerminalRegister.class);

			log.info("{}[{}] -> {}{}", terminalRegister.getSn(), websocket.getRemoteSocketAddress().getAddress(),
					terminalRegister.getMessageType(), message);

			// Build Terminal
			Terminal terminal = Terminal.builder().terminalId(terminalRegister.getSn()).webSocket(websocket)
					.terminalAddress(websocket.getRemoteSocketAddress().getAddress().getHostAddress()).build();
			terminal.getLock().lock();
			try {

				// Create TerminalOperationLog
				TerminalOperationLog terminalOperationLog = TerminalOperationLog.builder()
						.terminalId(terminal.getTerminalId()).terminal(terminal)
						.operationType(OperationType.DEVICE_CONNECT_TERMINAL).recordCount(0).recordFetched(0)
						.terminalOperationStatus(TerminalOperationStatus.IN_PROGRESS).build();

				// Save Terminal & Operation Log - IN PROGRESS
				terminalService.save(terminal);

				// Save new TerminalOperationLog
				terminalOperationLog = terminalService.save(terminalOperationLog);

				terminalOperationLog.setTerminal(terminal);

				// Save new TerminalOperationLog in Terminal Cache
				TerminalOperationCache.addTerminalOperation(terminalOperationLog, websocket);

				// Prepare Reply to TerminalRegister
				TerminalRegisterReply terminalRegisterReply = TerminalRegisterReply.builder().build();

				terminalService.doExecute(terminalOperationLog, terminalRegisterReply);

				log.info("{}[{}] <-> {}[{}]", terminal.getTerminalId(),
						terminal.getWebSocket().getRemoteSocketAddress(), InetAddress.getLocalHost().getHostName(),
						websocket.getLocalSocketAddress());
			} finally {
				terminal.getLock().unlock();
			}
		} catch (JsonProcessingException e3) {
			log.error("{} - Invalid JSON Data {}", websocket.getRemoteSocketAddress(), message);
		}
	}

	private void sendLog(WebSocket webSocket, String message) {
		try {
			SendLog sendLog = objectMapper.readValue(message, SendLog.class);

			log.info("{}[{}] -> {}{}", sendLog.getSn(), webSocket.getRemoteSocketAddress(), sendLog.getMessageType(),
					message);

			SendLogReply sendLogReply = SendLogReply.builder().count(sendLog.getCount()).logindex(sendLog.getLogindex())
					.build();

			Terminal terminal = TerminalOperationCache.getTerminal(sendLog.getSn());

			synchronized (terminal) {
				TerminalSend terminalSend = TerminalSend.builder().sendCommand(sendLog.getCmd())
						.terminalId(sendLog.getSn()).build();
				terminalSend = terminalSendLogService.saveTerminalSendLog(terminalSend);

				// Create New TerminalOperationLog
				TerminalOperationLog terminalOperationLog = TerminalOperationLog.builder()
						.terminalId(terminal.getTerminalId()).terminal(terminal)
						.operationType(OperationType.DEVICE_SENDLOG_OPERATION_TERMINAL).sendId(terminalSend.getSendId())
						.recordFetchOperation(true).recordCount(sendLog.getCount()).recordFetched(sendLog.getCount())
						.terminalOperationStatus(TerminalOperationStatus.IN_PROGRESS).build();

				// Save Terminal & Operation Log - IN PROGRESS
				terminalOperationLog = terminalService.save(terminalOperationLog);

				List<Attendance> attendanceList = sendLog.getAttendanceList(terminalSend);
				attendanceService.saveNewAttendances(attendanceList);

				terminalService.doExecute(terminalOperationLog, sendLogReply);
			}

		} catch (JsonProcessingException e) {
			log.error("{} - Invalid JSON Data {}", webSocket.getRemoteSocketAddress(), message);
		}
	}

	private void getAllLogReply(WebSocket webSocket, String message) {

		GetAllLogReply getAllLogReply;
		try {
			getAllLogReply = objectMapper.readValue(message, GetAllLogReply.class);
		} catch (JsonProcessingException e) {
			log.error("{} - Invalid JSON Data {}", webSocket.getRemoteSocketAddress(), message);
			return;
		}

		Terminal terminal = TerminalOperationCache.getTerminal(getAllLogReply.getSn());

		terminal.getLock().lock();

		TerminalOperationLog terminalOperationLog = TerminalOperationCache
				.getTerminalOperationLog(terminal.getTerminalId());

		log.info("{}[{}] -> {}{}", getAllLogReply.getSn(),
				webSocket.getRemoteSocketAddress().getAddress().getHostAddress(), getAllLogReply.getMessageType(),
				message);

		try {
			if (getAllLogReply.isEmptyReply()) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);

				terminalOperationLog = terminalService.save(terminalOperationLog);
				terminalOperationLog.setTerminal(terminal);

				// Save Terminal & Operation Log - IN PROGRESS
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);

				ServerPullLog serverPullLog = BiometricDataPullScheduler.getServerPullLogByTerminal(terminal);
				if (serverPullLog != null)
					serverPullLog.getPullLogLatch().countDown();// down latch

				log.warn("{}[{}] -> {} completed", terminal.getTerminalId(), webSocket.getRemoteSocketAddress(),
						MessageType.DEVICE_GETALLLOG_MSG);
			} else {
				GetAllLogReplyServerResponse getAllLogReplyServerResponse = GetAllLogReplyServerResponse.builder()
						.build();

				ServerPullLogKey serverPullLogKey = ServerPullLogKey.builder().pullId(terminalOperationLog.getPullId())
						.terminalId(terminalOperationLog.getTerminal().getTerminalId()).build();
				ServerPullLog serverPullLog = ServerPullLog.builder().serverPullLogKey(serverPullLogKey).build();
				terminalOperationLog.setRecordCount(getAllLogReply.getCount());
				terminalOperationLog.setRecordFetched(getAllLogReply.getTo());
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog, webSocket);
				terminalOperationLog = terminalService.save(terminalOperationLog);
				terminalOperationLog.setTerminal(terminal);
				List<Attendance> attendanceList = getAllLogReply.getAttendanceList(serverPullLog);
				attendanceService.saveAttendances(attendanceList);
				terminalService.doExecute(terminalOperationLog, getAllLogReplyServerResponse);
			}
		} finally {
			terminal.getLock().unlock();
		}

	}

}
