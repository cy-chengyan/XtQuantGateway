package chronika.xtquant.common.infra.converter;

import chronika.xtquant.common.infra.util.JsonUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public Map<String, Object> convertToEntityAttribute(String attribute) {
        return JsonUtil.jsonStringToMap(attribute);
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> dbData) {
        return JsonUtil.toJsonString(dbData);
    }

}
