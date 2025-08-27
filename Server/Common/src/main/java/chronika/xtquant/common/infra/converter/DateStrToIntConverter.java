package chronika.xtquant.common.infra.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DateStrToIntConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return Integer.parseInt(attribute.replace("-", ""));
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        String tmp = String.valueOf(dbData);
        return tmp.substring(0, 4) + "-" + tmp.substring(4, 6) + "-" + tmp.substring(6);
    }

}
