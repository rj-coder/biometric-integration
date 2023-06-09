package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.BioTerminalStatus;
import in.westerncoal.biometric.repository.BioTerminalRepository;
 import in.westerncoal.biometric.service.BioTerminalService;

@Service
public class BioTerminalServiceImpl implements BioTerminalService {
	@Autowired
	 BioTerminalRepository bioTerminalRepository;

	@Override
	public Terminal updateBioTerminal(Terminal bioTerminal) {
		return bioTerminalRepository.save(bioTerminal);

	}

	 

	@Override
	public void updateBioTerminal(String bioTerminalSn, BioTerminalStatus bioTerminalStatus) {
		 Terminal bioTerminal = Terminal.builder().bioTerminalSn(bioTerminalSn).bioTerminalStatus(bioTerminalStatus).build();
		 bioTerminalRepository.save(bioTerminal);
	}

	 

}
