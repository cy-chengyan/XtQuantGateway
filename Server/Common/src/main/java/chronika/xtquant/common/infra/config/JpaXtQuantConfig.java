package chronika.xtquant.common.infra.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "chronika.xtquant.common",
    entityManagerFactoryRef = "xtQuantEntityManagerFactory",
    transactionManagerRef = "xtQuantTransactionManager")
public class JpaXtQuantConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean xtQuantEntityManagerFactory(@Qualifier("xtQuantDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("chronika.xtquant.common");
        em.setPersistenceUnitName("xtquant");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Explicitly set the EntityManagerFactory interface to avoid conflict between
        // the EntityManagerFactory interfaces used by Spring and Hibernate.
        em.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return em;
    }

    @Bean
    public PlatformTransactionManager xtQuantTransactionManager(
        @Qualifier("xtQuantEntityManagerFactory") LocalContainerEntityManagerFactoryBean xtQuantEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(xtQuantEntityManagerFactory.getObject()));
    }

}
