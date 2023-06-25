package in.westerncoal.biometric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import in.westerncoal.biometric.enums.TerminalOperationStatus;
import in.westerncoal.biometric.job.BiometricDataPullScheduler;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationCache;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.service.TerminalService;
import jakarta.annotation.PreDestroy;

@SpringBootApplication
@EnableScheduling
public class BiometricIntegrationApplication {
	 

	public static void main(String[] args) {

		SpringApplication.run(BiometricIntegrationApplication.class, args);

	}

	
}
