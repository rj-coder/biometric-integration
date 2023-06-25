package in.westerncoal.biometric.service.impl;

import java.sql.Timestamp;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.westerncoal.biometric.enums.MessageType;
import in.westerncoal.biometric.enums.TerminalOperationStatus;
import in.westerncoal.biometric.enums.TerminalStatus;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPullLogKey;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.repository.ServerPullLogRepository;
import in.westerncoal.biometric.repository.TerminalOperationLogRepository;
import in.westerncoal.biometric.repository.TerminalRepository;
import in.westerncoal.biometric.server.operation.GetAllLogReplyServerResponse;
import in.westerncoal.biometric.server.operation.SendLogReply;
import in.westerncoal.biometric.server.operation.TerminalRegisterReply;
import in.westerncoal.biometric.service.ServerPullLogService;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.util.BioUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TerminalServiceImpl implements TerminalService {
	@Autowired
	TerminalRepository terminalRepository;

	@Autowired
	TerminalOperationLogRepository terminalOperationLogRepository;

	@Autowired
	ServerPullLogRepository serverPullLogRepository;
	
	@Autowired
	ServerPullLogService serverPullLogService;



	@Override
	@Transactional
	public void doExecute(TerminalOperationLog terminalOperationLog, TerminalRegisterReply terminalRegisterReply) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		try {
			String reply = BioUtil.getObjectMapper().writeValueAsString(terminalRegisterReply);
			terminal.getWebSocket().send(reply);
			terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);
			terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
			terminalOperationLog.setTerminal(terminal);// Update Terminal
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					terminalRegisterReply.getMessageType(), reply);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminal.setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
			terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
			terminalOperationLog.setTerminal(terminal);// Update terminal of Terminal Operation Log
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog, terminal.getWebSocket());// Update
																											// TerminalOperationCache
			terminalRepository.save(terminal);// Update Terminal Status

			log.warn("{}[{}] -/- {}{} : WebSocket not connected", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), terminalRegisterReply.getMessageType(), this);
		} catch (JsonProcessingException e) {
			log.error("{}[{}] -/- {}{} : JSON Parsing Error", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), terminalRegisterReply.getMessageType(), this);

		}
	}

	@Override
	@Transactional
	public void doExecute(TerminalOperationLog terminalOperationLog, SendLogReply sendLogReply) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		try {
			String reply = BioUtil.getObjectMapper().writeValueAsString(sendLogReply);

			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);

			terminal.getWebSocket().send(reply);
			terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
			terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
			terminalOperationLog.setTerminal(terminal);// Update Terminal
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					sendLogReply.getMessageType(), reply);
		} catch (JsonProcessingException e) {
			log.error("{}[{}] -/- {}{} : JSON Parsing Error", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), sendLogReply.getMessageType(), this);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminal.setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
			terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
			terminalOperationLog.setTerminal(terminal);// Update Terminal Operation Log
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog, terminal.getWebSocket());// update
			terminalRepository.save(terminal); // update terminal
			log.warn("{}[{}] -/- {}{} : WebSocket not connected", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), sendLogReply.getMessageType(), this);
		}
	}

	@Override
	public TerminalOperationLog findFirstByTerminalOrderByTerminalLastTimestampDesc(Terminal terminal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void doExecute(TerminalOperationLog terminalOperationLog, ServerPullLog serverPullLog) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		TerminalOperationLog cacheLog = TerminalOperationCache.getTerminalOperationLog(terminal);

		if (!cacheLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS) 
				&& terminal.getLock().tryLock()) {
			try {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.IN_PROGRESS);
				terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
				terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
				terminalOperationLog.setTerminal(terminal);// Update Terminal
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);

				ServerPullLogKey serverPullLogKey = ServerPullLogKey.builder().pullId(terminalOperationLog.getPullId())
						.terminalId(terminalOperationLog.getTerminal().getTerminalId()).build();
				serverPullLog.setServerPullLogKey(serverPullLogKey);
				
				//save new SERVER_PULL_LOG
				serverPullLogService.saveNew(serverPullLog);
				
				terminal.getWebSocket().send(serverPullLog.getPullCommand());

				log.info("{}[{}] <- {}{}", terminal.getTerminalId(),
						terminal.getWebSocket().getRemoteSocketAddress().getAddress().getHostAddress(),
						MessageType.DEVICE_GETALLLOG_MSG, serverPullLog.getPullCommand());
			} catch (WebsocketNotConnectedException e) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
				terminal.setTerminalStatus(TerminalStatus.INACTIVE);
				terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
				terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
				terminalOperationLog.setTerminal(terminal);// Update Terminal
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);// update terminal status
				terminalRepository.save(terminal);
				log.warn("{}[{}] -/- {}{} : WebSocket not connected", terminal.getTerminalId(),
						terminal.getWebSocket().getRemoteSocketAddress(), MessageType.DEVICE_GETALLLOG_MSG, this);
			} finally {
				terminal.getLock().unlock();
			}
		} else
			log.warn("Previous TerminalOperation in Progress or could not acquire lock: {}", terminal.getTerminalId());
	}

	@Override
	@Transactional
	public void doExecute(TerminalOperationLog terminalOperationLog,
			GetAllLogReplyServerResponse getAllLogReplyServerResponse) {
		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		terminal.getLock().lock();
		try {
			String reply = BioUtil.getObjectMapper().writeValueAsString(getAllLogReplyServerResponse);

			terminal.getWebSocket().send(reply);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					getAllLogReplyServerResponse.getMessageType(), reply);
		} catch (JsonProcessingException e) {
			log.error("{}[{}] -/- {}{} : JSON Parsing Error", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), getAllLogReplyServerResponse.getMessageType(),
					getAllLogReplyServerResponse);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminalOperationLog.getTerminal().setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog.setLastOperationTime(new Timestamp(System.currentTimeMillis()));
			terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);
			terminalOperationLog.setTerminal(terminal);// Update Terminal
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog, terminal.getWebSocket());// update
																											// terminal
			log.warn("{}[{}] -/- {}{} : WebSocket not connected", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), getAllLogReplyServerResponse.getMessageType(),
					getAllLogReplyServerResponse);
		} finally {
			terminal.getLock().unlock();

		}

	}

	@Override
	@Transactional
	public TerminalOperationLog save(TerminalOperationLog terminalOperationLog) {
		return terminalOperationLogRepository.save(terminalOperationLog);
	}

	@Override
	@Transactional
	public void updateTerminalOperationLog(TerminalOperationLog terminalOperationLog) {
		terminalOperationLogRepository.updateTerminalOperationLog(terminalOperationLog);

	}

	@Override
	@Transactional
	public void updateTerminal(Terminal terminal) {
		terminalRepository.updateTerminal(terminal);
	}

	@Override
	public void save(Terminal terminal) {
		terminalRepository.save(terminal);
	}

}
