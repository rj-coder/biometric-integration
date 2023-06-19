package in.westerncoal.biometric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import in.westerncoal.biometric.config.ShutdownHandler;
import in.westerncoal.biometric.service.TerminalService;

@SpringBootApplication
@EnableScheduling
public class BiometricIntegrationApplication  implements ApplicationListener<ContextClosedEvent> {
	@Autowired
	TerminalService bioTerminalService;
 
 	@Autowired
 	ShutdownHandler shutdownHandler;

	public static void main(String[] args) {
		ConfigurableApplicationContext a = SpringApplication.run(BiometricIntegrationApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		// TODO Auto-generated method stub
		
	}
}
