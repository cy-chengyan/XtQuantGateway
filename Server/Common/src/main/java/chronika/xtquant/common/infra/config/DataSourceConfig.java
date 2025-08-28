package chronika.xtquant.common.infra.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.xtquant")
    public DataSourceProperties xtQuantDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource xtQuantDataSource() {
        return xtQuantDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.quotation")
    public DataSourceProperties quotationDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource quotationDataSource() {
        return quotationDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
    }

}
