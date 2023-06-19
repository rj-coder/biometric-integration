package in.westerncoal.biometric.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.model.TerminalOperationStatus;
import in.westerncoal.biometric.repository.TerminalOperationLogRepository;
import in.westerncoal.biometric.service.TerminalService;
import jakarta.annotation.PreDestroy;

@Component
public class ShutdownHandler {

	@Autowired
	private TerminalService terminalService;

	@PreDestroy
	public void onShutdown() {
		// Retrieve the last terminal operation
		for (Terminal terminal : TerminalOperationCache.getActiveTerminals()) {
			TerminalOperationLog terminalOperationLog = TerminalOperationCache.getTerminalOperationLog(terminal);
			if (terminalOperationLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS)) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);
				terminalService.save(terminalOperationLog);
			} else
				continue;

		}
	}
}
