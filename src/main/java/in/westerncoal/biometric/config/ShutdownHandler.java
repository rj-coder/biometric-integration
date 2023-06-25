package in.westerncoal.biometric.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import in.westerncoal.biometric.enums.TerminalOperationStatus;
import in.westerncoal.biometric.job.BiometricDataPullScheduler;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.service.TerminalService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShutdownHandler {

	@Autowired
	private TerminalService terminalService;

	@PreDestroy
	public void onShutdown() {
		log.info("Biometric Integration Server shutdown IN PROGRESS, releasing,clean up of resources in progress ");
		// Retrieve the last terminal operation
		for (Terminal terminal : TerminalOperationCache.getActiveTerminals()) {
			TerminalOperationLog terminalOperationLog = TerminalOperationCache.getTerminalOperationLog(terminal);
			if (terminalOperationLog.getTerminalOperationStatus().equals(TerminalOperationStatus.IN_PROGRESS)) {
				terminalOperationLog.setTerminalOperationStatus(TerminalOperationStatus.ERROR);
				TerminalOperationCache.updateTerminalOperation(terminalOperationLog);
				terminalService.save(terminalOperationLog);
				ServerPullLog serverPullLog = BiometricDataPullScheduler.getServerPullLogByTerminal(terminal);
				if (serverPullLog != null)
					serverPullLog.getPullLogLatch().countDown();// down latch
			} else
				continue;
		}
		log.info("Biometric Integration Server shut down COMPLETED");
	}
}
