package in.westerncoal.biometric.service.impl;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.client.operation.GetAllLogReply;
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
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.util.BioUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

	@PersistenceContext
	EntityManager em;

	@Override
	public void setTerminalInactive(String serialNo) {
		terminalRepository.update(serialNo);
	}

	@Override
	public void save(Terminal terminal) {
		terminalRepository.save(terminal);

	}

	@Override
	@Transactional
	public void doExecute(TerminalOperationLog terminalOperationLog, TerminalRegisterReply terminalRegisterReply) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		try {
			String reply = BioUtil.getObjectMapper().writeValueAsString(terminalRegisterReply);
			terminal.getWebSocket().send(reply);
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);
			terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
			TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(),terminalOperationLog);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					terminalRegisterReply.getMessageType(), reply);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminal.setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog, terminal.getWebSocket());
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
			TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(),terminalOperationLog);
			terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
			TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(), terminalOperationLog);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					sendLogReply.getMessageType(), reply);
		} catch (JsonProcessingException e) {
			log.error("{}[{}] -/- {}{} : JSON Parsing Error", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), sendLogReply.getMessageType(), this);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminal.setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
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
	public void doExecute(TerminalOperationLog terminalOperationLog, String pullCommand) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		TerminalOperationLog cacheLog = TerminalOperationCache.getTerminalOperationLog(terminal);

		if (!cacheLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS)
				&& terminal.getLock().tryLock()) {
			try {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.IN_PROGRESS);
				terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);

				TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(),terminalOperationLog);

				ServerPullLogKey serverPullLogKey = ServerPullLogKey.builder().pullId(terminalOperationLog.getPullId())

						.terminalId(terminalOperationLog.getTerminal().getTerminalId()).build();
				ServerPullLog serverPullLog = ServerPullLog.builder().serverPullLogKey(serverPullLogKey)
						.pullCommand(pullCommand).build();
				serverPullLogRepository.save(serverPullLog);

				terminal.getWebSocket().send(pullCommand);
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.COMPLETED);
				terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
				TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(),terminalOperationLog);

				log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
						MessageType.DEVICE_GETALLLOG_MSG, pullCommand);
			} catch (WebsocketNotConnectedException e) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
				terminal.setTerminalStatus(TerminalStatus.INACTIVE);
				terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
				TerminalOperationCache.updateTerminalOperation(terminal.getTerminalId(),terminalOperationLog);// update terminal status
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
	public void doExecute(TerminalOperationLog terminalOperationLog, GetAllLogReply getAllLogReply) {

		Terminal terminal = TerminalOperationCache.getTerminal(terminalOperationLog.getTerminal().getTerminalId());
		terminal.getLock().lock();
		try {
			String reply = BioUtil.getObjectMapper().writeValueAsString(getAllLogReply);

			terminal.getWebSocket().send(reply);

			log.info("{}[{}] <- {}{}", terminal.getTerminalId(), terminal.getWebSocket().getRemoteSocketAddress(),
					getAllLogReply.getMessageType(), reply);
		} catch (JsonProcessingException e) {
			log.error("{}[{}] -/- {}{} : JSON Parsing Error", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), getAllLogReply.getMessageType(), this);
		} catch (WebsocketNotConnectedException e) {
			terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
			terminal.setTerminalStatus(TerminalStatus.INACTIVE);
			terminalOperationLog = terminalOperationLogRepository.save(terminalOperationLog);
			TerminalOperationCache.updateTerminalOperation(terminalOperationLog, terminal.getWebSocket());// update
																											// terminal
			terminalRepository.save(terminal);
			log.warn("{}[{}] -/- {}{} : WebSocket not connected", terminal.getTerminalId(),
					terminal.getWebSocket().getRemoteSocketAddress(), getAllLogReply.getMessageType(), this);
		} finally {
			terminal.getLock().unlock();

		}
	}

	@Override
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
			terminalOperationLogRepository.save(terminalOperationLog);
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
	public TerminalOperationLog save(TerminalOperationLog terminalOperationLog) {

		return terminalOperationLogRepository.save(terminalOperationLog);
	}

}
