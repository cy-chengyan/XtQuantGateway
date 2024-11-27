package chronika.xtquant.tradeapi.config;

import chronika.xtquant.common.infra.util.JsonUtil;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
    servers = {
            @Server(url = "http://127.0.0.1:1090/api", description = "Local server"),
            @Server(url = "http://trade-gw-02:1090/api", description = "Production server")
    },
    info = @Info(title = "Chronika XtQuant API")
)

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi ginkgoApi(){
        String[] paths = { "/**" };
        String[] packagedToMatch = { "chronika.xtquant.tradeapi.controller" };
        return GroupedOpenApi.builder()
                .group("Chronika XtQuant API")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public ModelResolver modelResolver() {
        return new ModelResolver(JsonUtil.getObjectMapper());
    }

}
