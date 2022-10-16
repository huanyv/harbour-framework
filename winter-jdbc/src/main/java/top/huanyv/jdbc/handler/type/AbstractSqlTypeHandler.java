package top.huanyv.jdbc.handler.type;

import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/10/15 20:31
 */
public abstract class AbstractSqlTypeHandler implements TypeHandler {

    protected String sql;

    protected Object[] args;

    protected Method method;

    public AbstractSqlTypeHandler() {
    }

    public AbstractSqlTypeHandler(String sql, Object[] args, Method method) {
        this.sql = sql;
        this.args = args;
        this.method = method;
    }

    public abstract boolean isType();

}
