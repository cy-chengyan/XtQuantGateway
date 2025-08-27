package chronika.xtquant.common.infra.converter;

import chronika.xtquant.common.infra.util.DateUtil;
import jakarta.persistence.AttributeConverter;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class IntToDateConverter implements AttributeConverter<Integer, Date> {

    @Override
    public Integer convertToEntityAttribute(Date attribute) {
        return Integer.parseInt(DateFormatUtils.format(attribute, DateUtil.YYYYMMDD));
    }

    @Override
    public Date convertToDatabaseColumn(Integer dbData) {
        return java.sql.Date.valueOf(DateUtil.parseIntDateToStr(dbData));
    }

}
