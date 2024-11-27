package chronika.xtquant.tradeapi.config;

import chronika.xtquant.tradeapi.filter.ApiLogFilter;
import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;


@Configuration
public class ApiLogConfig {

    @Bean
    public FilterRegistrationBean<?> registerRequestResponseLoggingFilter() {
        FilterRegistrationBean<ApiLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(apiLogFilter());
        registration.setUrlPatterns(Lists.newArrayList("/*"));
        registration.setName("apiLogFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public ApiLogFilter apiLogFilter() {
        return new ApiLogFilter();
    }

}
