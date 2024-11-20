package chronika.xtquant.common.infra.util;

import chronika.xtquant.common.infra.enums.AppEnvType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Scope("singleton")
@Lazy(false)
@Component
public class CkAppContext implements ApplicationContextAware {

    private static ApplicationContext ctx;
    private static volatile AppEnvType env = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static Object getBean(String name) {
        return ctx.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return ctx.getBeansOfType(clazz);
    }

    public static AppEnvType getCurrentEnvProfile() {
        if (env == null) {
            synchronized (CkAppContext.class) {
                if (env == null) {
                    Environment environment = ctx.getEnvironment();
                    String active = environment.getProperty("spring.profiles.active");
                    env = AppEnvType.getByAppEnvString(active);
                }
            }
        }
        return env;
    }

    public static boolean isProductOrStageEnv() {
        env = getCurrentEnvProfile();
        return env.equals(AppEnvType.PROD) || env.equals(AppEnvType.STAGE);
    }

    public static boolean isProductEnv() {
        return getCurrentEnvProfile().equals(AppEnvType.PROD);
    }

    public static boolean isStageEnv() {
        return getCurrentEnvProfile().equals(AppEnvType.STAGE);
    }

    public static boolean isTestEnv() {
        return getCurrentEnvProfile().equals(AppEnvType.TEST);
    }

}
