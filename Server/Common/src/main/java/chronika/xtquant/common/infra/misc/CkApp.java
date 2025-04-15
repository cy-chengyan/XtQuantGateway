package chronika.xtquant.common.infra.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CkApp {

    private final String id;
    private final String name;
    private final String version;

    @Autowired
    private CkApp(@Value("${app.id}") String appId, @Value("${app.name}") String appName, @Value("${app.version}") String appVersion) {
        this.id = appId;
        this.name = appName;
        this.version = appVersion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
