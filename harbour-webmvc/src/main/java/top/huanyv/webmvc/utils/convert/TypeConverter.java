package top.huanyv.webmvc.utils.convert;

import top.huanyv.webmvc.utils.ClassDesc;

/**
 * 类型转换器
 *
 * @author huanyv
 * @date 2022/11/10 16:29
 */
public interface TypeConverter {

    Object convert(Object source, ClassDesc targetClassDesc);

    boolean isType(Object source, ClassDesc targetClassDesc);

}
