package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.TerminalSendLog;
import in.westerncoal.biometric.repository.TerminalSendLogRepository;
import in.westerncoal.biometric.service.TerminalSendLogService;

@Service
public class TerminalSendLogServiceImpl implements TerminalSendLogService {

	@Autowired
	TerminalSendLogRepository terminalSendLogRepository;

	@Override
	public TerminalSendLog saveTerminalSendLog(TerminalSendLog terminalSendLog) {
		return terminalSendLogRepository.save(terminalSendLog);
	}

}
