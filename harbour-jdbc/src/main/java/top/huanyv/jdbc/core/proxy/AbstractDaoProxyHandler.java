package top.huanyv.jdbc.core.proxy;

import top.huanyv.jdbc.annotation.*;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.DefaultSqlContext;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.jdbc.handler.type.SqlTypeHandler;
import top.huanyv.jdbc.handler.type.SqlTypeHandlerComposite;
import top.huanyv.jdbc.util.BaseDaoUtil;
import top.huanyv.tools.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author admin
 * @date 2022/7/23 15:05
 */
public abstract class AbstractDaoProxyHandler implements BaseDao<Object> {

    private Object proxy;

    public Object doBaseDao(Object proxy, Method method, Object[] args) throws Throwable {
        this.proxy = proxy;
        return method.invoke(this, args);
    }

    public Object doAnnotation(Method method, Object[] args) throws SQLException {
        if (method.isAnnotationPresent(Select.class)) {
            return doSelect(method, args);
        }
        Update update = method.getAnnotation(Update.class);
        if (update != null) {
            return doUpdate(update.value(), method, args);
        }
        Insert insert = method.getAnnotation(Insert.class);
        if (insert != null ) {
            if (insert.getId()) {
                return SqlContextFactory.getSqlContext().insert(insert.value(), args);
            } else {
                return doUpdate(insert.value(), method, args);
            }
        }
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null) {
            return doUpdate(delete.value(), method, args);
        }
        return null;
    }

    public Object doSelect(Method method, Object[] args) throws SQLException {
        String sql = method.getAnnotation(Select.class).value();
        SqlTypeHandler sqlTypeHandler = new SqlTypeHandlerComposite();
        return sqlTypeHandler.handle(sql, args, method);
    }

    public int doUpdate(String sql, Method method, Object[] args) throws SQLException {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql, args);
    }

    @Override
    public List<Object> selectAll() {
        Class<?> cls = proxy.getClass();
        Class<?> beanType = BaseDaoUtil.getType(cls);
        Assert.notNull(beanType, "BaseDao not set <generic>!");

        String tableName = BaseDaoUtil.getTableName(beanType);
        String sql = "select * from " + tableName;
        return SqlContextFactory.getSqlContext().selectList((Class<Object>) beanType, sql);
    }

    @Override
    public Object selectById(Number id) {
        Assert.notNull(id, "'id' must not be null.");
        Class<?> cls = proxy.getClass();
        Class<?> beanType = BaseDaoUtil.getType(cls);
        Assert.notNull(beanType, "BaseDao not set <generic>!");

        String tableId = BaseDaoUtil.getTableId(beanType);
        String tableName = BaseDaoUtil.getTableName(beanType);

        StringBuilder sql = new StringBuilder("select * from ")
                .append(tableName).append(" where ").append(tableId).append(" = ?");
        return SqlContextFactory.getSqlContext().selectRow(beanType, sql.toString(), id);
    }

    @Override
    public int insert(Object o) {
        Assert.notNull(o, "object must not be null.");
        Class<?> cls = o.getClass();
        // 参数
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("insert into ").append(BaseDaoUtil.getTableName(cls));
        // (column1, column2, column3, .....)
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        // (?, ?, ?, ......)
        StringJoiner values = new StringJoiner(", ", "(", ")");

        String tableId = BaseDaoUtil.getTableId(cls);
        try {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                // 获取属性值
                Object arg = field.get(o);
                // 非ID
                if (arg != null && !field.getName().equals(tableId)) {
                    Column column = field.getAnnotation(Column.class);
                    String columnName = column != null ? column.value() : field.getName();
                    columns.add(columnName);
                    values.add("?");
                    args.add(arg);
                }
            }
            sql.append(columns).append(" values").append(values);

            SqlContext sqlContext = SqlContextFactory.getSqlContext();
            int id = (int) sqlContext.insert(sql.toString(), args.toArray());
            // 设置ID
            Field idField = BaseDaoUtil.getIdField(cls);
            idField.setAccessible(true);
            idField.set(o, id);
            return id != -1 ? 1 : -1;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateById(Object o) {
        Assert.notNull(o, "object must not be null.");
        try {
            Class<?> cls = o.getClass();
            // 获取表名
            String tableName = BaseDaoUtil.getTableName(cls);
            // 获取ID列名
            String tableIdName = BaseDaoUtil.getTableId(cls);
            // sql语句
            StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
            // column1 = ?, column2 = ?, ......
            StringJoiner columns = new StringJoiner(", ");
            // 参数
            List<Object> args = new ArrayList<>();
            Object tableId = null;
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(o);
                Column column = field.getAnnotation(Column.class);
                String columnName = column != null ? column.value() : field.getName();
                if (columnName.equals(tableIdName)) {
                    tableId = val;
                }
                if (!columnName.equals(tableIdName) && val != null) {
                    columns.add(columnName + " = ?");
                    args.add(val);
                }
            }
            sql.append(columns);
            // 指定id
            sql.append(" where ").append(tableIdName).append(" = ?");
            Assert.notNull(tableId, "table id not set!");
            args.add(tableId);

            SqlContext sqlContext = SqlContextFactory.getSqlContext();
            return sqlContext.update(sql.toString(), args.toArray());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteById(Number id) {
        Assert.notNull(id, "'id' must not be null.");
        Class<?> cls = proxy.getClass();
        Class<?> type = BaseDaoUtil.getType(cls);
        Assert.notNull(type, "BaseDao not set <generic>!");

        String tableId = BaseDaoUtil.getTableId(type);
        String tableName = BaseDaoUtil.getTableName(type);
        StringBuilder sql = new StringBuilder("delete from ").append(tableName)
                .append(" where ").append(tableId).append(" = ?");

        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql.toString(), id);
    }

}
