package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalStatus;
import in.westerncoal.biometric.repository.TerminalRepository;
import in.westerncoal.biometric.service.TerminalService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TerminalServiceImpl implements TerminalService {
	@Autowired
	TerminalRepository terminalRepository;

	@PersistenceContext
	EntityManager em;

	@Override
	public Terminal updateTerminal(Terminal terminal) {
		return terminalRepository.save(terminal);

	}

	@Override
	public void updateBioTerminal(String bioTerminalSn, TerminalStatus bioTerminalStatus) {
		Terminal bioTerminal = Terminal.builder().terminalId(bioTerminalSn).terminalStatus(bioTerminalStatus)
				.build();
		terminalRepository.save(bioTerminal);
	}

	@Override
	public void setTerminalInactive(String serialNo) {		
		terminalRepository.update(serialNo);
	}

}
