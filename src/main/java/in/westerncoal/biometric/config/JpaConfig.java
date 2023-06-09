//package in.westerncoal.biometric.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@Configuration
//@EnableJpaRepositories(basePackages = "in.westerncoal.biometric.repository")
//public class JpaConfig {
//
//	@Bean
//	public DataSource getDataSource() {
//		return DataSourceBuilder.create().driverClassName("org.h2.Driver").url("jdbc:h2:~/bio").username("sa")
//				.password("").build();
//	}
//
//}
