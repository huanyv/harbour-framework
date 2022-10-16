package top.huanyv.jdbc.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/15 20:48
 */
public class SqlTypeHandlerManager extends AbstractSqlTypeHandler{

    private List<AbstractSqlTypeHandler> sqlTypeHandlers;

    public SqlTypeHandlerManager(String sql, Object[] args, Method method) {
        super(sql, args, method);
        this.sqlTypeHandlers = new ArrayList<>();
        sqlTypeHandlers.add(new ListSqlTypeHandler(sql, args, method));
        sqlTypeHandlers.add(new CustomSqlTypeHandler(sql, args, method));
        sqlTypeHandlers.add(new SingleValueSqlTypeHandler(sql, args, method));
    }

    @Override
    public Object handle() {
        try {
            for (AbstractSqlTypeHandler handler : this.sqlTypeHandlers) {
                if (handler.isType()) {
                    return handler.handle();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean isType() {
        return sqlTypeHandlers.stream().anyMatch(handler -> handler.isType());
    }
}
