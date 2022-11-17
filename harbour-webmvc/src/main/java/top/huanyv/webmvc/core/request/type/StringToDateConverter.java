package top.huanyv.webmvc.core.request.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.utils.ClassDesc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author huanyv
 * @date 2022/11/11 10:08
 */
public class StringToDateConverter implements TypeConverter<String, Date> {


    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

    @Override
    public Date convert(String source, ClassDesc targetClassDesc) {
        String format = "yyyy-MM-dd";
        JsonFormat dateFormatter = targetClassDesc.getAnnotation(JsonFormat.class);
        if (dateFormatter != null && StringUtil.hasText(dateFormatter.pattern())) {
            format = dateFormatter.pattern();
        }
        DATE_FORMAT.applyPattern(format);
        try {
            return DATE_FORMAT.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isType(Class<?> sourceType, Class<?> targetType) {
        return String.class.equals(sourceType) && Date.class.equals(targetType);
    }

}
