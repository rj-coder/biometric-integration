package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.server.operation.GetAllLogReplyServerResponse;
import in.westerncoal.biometric.server.operation.SendLogReply;
import in.westerncoal.biometric.server.operation.TerminalRegisterReply;

@Component
public interface TerminalService {

	void save(Terminal terminal);
	
	void updateTerminal(Terminal terminal);

	TerminalOperationLog save(TerminalOperationLog terminalOperationLog);

	void updateTerminalOperationLog(TerminalOperationLog terminalOperationLog);

	TerminalOperationLog findFirstByTerminalOrderByTerminalLastTimestampDesc(Terminal terminal);

	void doExecute(TerminalOperationLog terminalOperationLog, TerminalRegisterReply terminalRegisterReply);

	void doExecute(TerminalOperationLog terminalOperationLog, SendLogReply sendLogReply);

	void doExecute(TerminalOperationLog terminalOperationLog, ServerPullLog serverPullLog);

	void doExecute(TerminalOperationLog terminalOperationLog,
			GetAllLogReplyServerResponse getAllLogReplyServerResponse);
}
