package chronika.xtquant.common.infra.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.lettuce.pool.max-active}")
    private int maxActive;
    @Value("${spring.data.redis.lettuce.pool.max-wait}")
    private Duration maxWait;
    @Value("${spring.data.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.data.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactoryDefault(@Value("${spring.redis-default.host}")String host,
                                                                  @Value("${spring.redis-default.port}")int port,
                                                                  @Value("${spring.redis-default.database}")int database,
                                                                  @Value("${spring.redis-default.password}")String password,
                                                                  @Value("${spring.redis-default.timeout}")Duration timeout,
                                                                  @Value("${spring.redis-default.ssl}")Boolean ssl) {
        return createConnectionFactory(host, port, database, password, timeout, ssl, maxActive, maxWait, maxIdle, minIdle);
    }

    // 默认redis
    // @Primary
    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplateDefault(@Autowired RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 创建lettuce配置Redis连接工厂
     */
    public static RedisConnectionFactory createConnectionFactory(String host, int port, int database, String password, Duration timeout, Boolean ssl,
                                                                 int maxActive, Duration maxWait, int maxIdle, int minIdle) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        if (StringUtils.hasLength(password)) {
            configuration.setPassword(password);
        }
        if (database != 0) {
            configuration.setDatabase(database);
        }

        GenericObjectPoolConfig<?> genericObjectPoolConfig = new GenericObjectPoolConfig<LettuceConnectionFactory>();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(maxWait);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder()
                .commandTimeout(timeout)
                .poolConfig(genericObjectPoolConfig);
        if (ssl) {
            builder.useSsl();
        }

        LettuceClientConfiguration clientConfig = builder.build();
        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }

}
