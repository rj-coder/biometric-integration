package in.westerncoal.biometric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import in.westerncoal.biometric.config.ShutdownHandler;
import in.westerncoal.biometric.service.TerminalService;

@SpringBootApplication
@EnableScheduling
public class BiometricIntegrationApplication {
	@Autowired
	TerminalService bioTerminalService;

	@Autowired
	ShutdownHandler shutdownHandler;

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(BiometricIntegrationApplication.class, args);
		app.registerShutdownHook();
	}

}
