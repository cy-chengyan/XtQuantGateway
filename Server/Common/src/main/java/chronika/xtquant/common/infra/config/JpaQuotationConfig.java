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
@EnableJpaRepositories(basePackages = "chronika.quotation",
    entityManagerFactoryRef = "quotationEntityManagerFactory",
    transactionManagerRef = "quotationTransactionManager")
public class JpaQuotationConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean quotationEntityManagerFactory(@Qualifier("quotationDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("chronika.quotation");
        em.setPersistenceUnitName("quotation");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Explicitly set the EntityManagerFactory interface to avoid conflict between
        // the EntityManagerFactory interfaces used by Spring and Hibernate.
        em.setEntityManagerFactoryInterface(EntityManagerFactory.class);

        return em;
    }

    @Bean
    public PlatformTransactionManager quotationTransactionManager(
        @Qualifier("quotationEntityManagerFactory") LocalContainerEntityManagerFactoryBean quotationEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(quotationEntityManagerFactory.getObject()));
    }

}
