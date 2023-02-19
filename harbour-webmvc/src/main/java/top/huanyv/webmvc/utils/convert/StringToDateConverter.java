package top.huanyv.webmvc.utils.convert;

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
public class StringToDateConverter implements TypeConverter {
    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        String format = "yyyy-MM-dd";
        JsonFormat dateFormatter = targetClassDesc.getAnnotation(JsonFormat.class);
        if (dateFormatter != null && StringUtil.hasText(dateFormatter.pattern())) {
            format = dateFormatter.pattern();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse((String) source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        return source instanceof String && Date.class.equals(targetClassDesc.getType());
    }
}
