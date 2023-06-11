package in.westerncoal.biometric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import in.westerncoal.biometric.service.TerminalService;

@SpringBootApplication
@EnableScheduling
public class BiometricIntegrationApplication {
	@Autowired
	TerminalService bioTerminalService;

	public static void main(String[] args) {
		SpringApplication.run(BiometricIntegrationApplication.class, args);
	}
}
