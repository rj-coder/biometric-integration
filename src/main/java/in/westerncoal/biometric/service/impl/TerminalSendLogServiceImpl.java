package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.TerminalSend;
import in.westerncoal.biometric.repository.TerminalSendLogRepository;
import in.westerncoal.biometric.service.TerminalSendLogService;

@Service
public class TerminalSendLogServiceImpl implements TerminalSendLogService {

	@Autowired
	TerminalSendLogRepository terminalSendLogRepository;

	@Override
	public TerminalSend saveTerminalSendLog(TerminalSend terminalSendLog) {
		return terminalSendLogRepository.save(terminalSendLog);
	}

}
