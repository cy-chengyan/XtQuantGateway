package chronika.xtquant.tradeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan({"chronika.xtquant.common"})
@EnableJpaRepositories({"chronika.xtquant.common"})
@ComponentScan({"chronika.xtquant.tradeapi", "chronika.xtquant.common", "chronika.xtquant.feedparser"})
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        app.run(args);
    }

}

