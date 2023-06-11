package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalStatus;

@Component
public interface TerminalService {

	Terminal updateTerminal(Terminal bioTerminal);

	void updateBioTerminal(String bioTerminalSn, TerminalStatus inactive);

	void setTerminalInactive(String serialNo);
}
