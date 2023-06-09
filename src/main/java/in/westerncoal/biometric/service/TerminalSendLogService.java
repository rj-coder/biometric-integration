package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.TerminalSendLog;

@Component
public interface TerminalSendLogService {
	TerminalSendLog saveTerminalSendLog(TerminalSendLog terminalSendLog);

}
