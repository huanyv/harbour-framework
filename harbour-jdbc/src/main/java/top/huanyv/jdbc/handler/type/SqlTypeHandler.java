package top.huanyv.jdbc.handler.type;

import java.lang.reflect.Method;

/**
 * 类型处理程序
 *
 * @author huanyv
 * @date 2022/10/15 17:16
 */
public interface SqlTypeHandler {

    Object handle(String sql, Object[] args, Method method);

    boolean isType(Method method);
}
