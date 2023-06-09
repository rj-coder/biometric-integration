package in.westerncoal.biometric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import in.westerncoal.biometric.service.BioTerminalService;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class BiometricIntegrationApplication {
	@Autowired
	BioTerminalService bioTerminalService;

	public static void main(String[] args) {
		SpringApplication.run(BiometricIntegrationApplication.class, args);
	}
//
//	@Bean(initMethod = "start", destroyMethod = "stop")
//	Server h2Server() throws SQLException {
//		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
//	}

}
