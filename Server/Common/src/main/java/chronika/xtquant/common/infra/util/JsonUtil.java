package chronika.xtquant.common.infra.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonUtil {

    private static final ObjectMapper objMapper = new ObjectMapper();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonUtil.class);

    static {
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // objMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        // objMapper.registerModules(
        //     new SimpleModule().addSerializer(BigDecimal.class, new GkBigDecimalSerializer())
        // );
    }

    public static ObjectMapper getObjectMapper() {
        return objMapper;
    }

    public static String toJsonString(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return objMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        if (!StringUtils.hasLength(jsonString)) {
            return null;
        }
        try {
            return objMapper.readValue(jsonString, HashMap.class);
        } catch (Exception e) {
            log.error("jsonStringToMap failed: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
        if (!StringUtils.hasLength(jsonString)) {
            return null;
        }
        try {
            return objMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("jsonStringToObject failed: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        String json = JsonUtil.toJsonString(map);
        return jsonStringToObject(json, clazz);
    }

    public static Map<String, Object> objectToMap(Object obj) {
        String json = JsonUtil.toJsonString(obj);
        return jsonStringToMap(json);
    }

}
