package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.TerminalSend;

@Component
public interface TerminalSendLogService {
	TerminalSend saveTerminalSendLog(TerminalSend terminalSendLog);

}
