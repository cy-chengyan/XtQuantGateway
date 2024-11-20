package chronika.xtquant.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.context.annotation.ComponentScan;

@EntityScan({"chronika.xtquant.common"})
@EnableJpaRepositories({"chronika.xtquant.common"})
// @ComponentScan({"chronika.juequant.core"})
@SpringBootApplication
public class DependOnApplicationTest {

    // 这个类是其它依赖 Application 环境的测试类的基础，由于类上面有一些注解，所以会自动加载，不需要在这里面添加任何东西
    // 有了这个类，其它测试类写起来会很简单，直接在类名前加个 @SpringBootTest() 注解即可

}
