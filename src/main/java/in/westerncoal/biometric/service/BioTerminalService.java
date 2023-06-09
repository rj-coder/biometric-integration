package in.westerncoal.biometric.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.BioTerminalStatus;
import in.westerncoal.biometric.repository.TerminalSendLogRepository;

@Component
public interface BioTerminalService {

	Terminal updateBioTerminal(Terminal bioTerminal);

	void updateBioTerminal(String bioTerminalSn, BioTerminalStatus inactive);
}
