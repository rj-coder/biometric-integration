//package in.westerncoal.biometric.config;
//
//import javax.sql.DataSource;
//
//import org.hibernate.jpa.HibernatePersistenceProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "in.westerncoal.biometric")
//public class JpaConfig {
//
//	@Autowired
//	private DataSource dataSource;
//
//	@Bean
//	@Primary
//	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//		em.setDataSource(dataSource);
//		em.setPackagesToScan("in.westerncoal.biometric");
//		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//		return em;
//	}
//
//	@Bean
//	PlatformTransactionManager transactionManager() {
//		JpaTransactionManager tm = new JpaTransactionManager();
//		tm.setEntityManagerFactory(entityManagerFactory().getObject());
//		return tm;
//	}
//}
