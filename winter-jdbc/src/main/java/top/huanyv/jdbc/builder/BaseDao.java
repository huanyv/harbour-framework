package top.huanyv.jdbc.builder;

import javafx.beans.binding.ObjectExpression;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.jdbc.util.BaseDaoUtil;
import top.huanyv.utils.Assert;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author huanyv
 * @date 2022/8/31 19:58
 */
public interface BaseDao<T> {

    default List<T> selectAll() {
        Class<?> beanType = BaseDaoUtil.getType(this.getClass());
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        List objects = new Select().from(beanType).selectList();
        return objects;
    }

    default T selectById(Number id) {
        Class<?> beanType = BaseDaoUtil.getType(this.getClass());
        String tableId = BaseDaoUtil.getTableId(beanType);
        Object o = new Select().from(beanType).where().append(tableId + " = ?", id).selectRow();
        return (T) o;
    }

    default int insert(T t) {
        Map<String, Object> data = new HashMap<>();
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("insert into ").append(BaseDaoUtil.getTableName(t.getClass()));
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        StringJoiner values = new StringJoiner(", ", "(", ")");
        try {
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object o = field.get(t);
                if (o != null) {
                    String columnName = field.getName();
                    columns.add(columnName);
                    values.add("?");
                    args.add(o);
                }
            }
            sql.append(columns).append(" values").append(values);
            SqlContext sqlContext = SqlContextFactory.getSqlContext();
            return sqlContext.update(sql.toString(), args.toArray());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    default int updateById(T t) {
        try {
            Class<?> clazz = t.getClass();
            // 获取表名
            String tableName = BaseDaoUtil.getTableName(clazz);
            // sql语句
            StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
            // column1 = ?, column2 = ?, ......
            StringJoiner columns = new StringJoiner(", ");
            List<Object> args = new ArrayList<>();
            String tableIdName = BaseDaoUtil.getTableId(clazz);
            Object tableId = null;
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(t);
                if (field.getName().equals(tableIdName)) {
                    tableId = val;
                }
                if (!field.getName().equals(tableIdName) && val != null) {
                    columns.add(field.getName() + " = ?");
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

    default int deleteById(Number id) {
        Class<?> type = BaseDaoUtil.getType(this.getClass());
        String tableId = BaseDaoUtil.getTableId(type);
        String tableName = BaseDaoUtil.getTableName(type);
        StringBuilder sql = new StringBuilder("delete from ").append(tableName)
                .append(" where ").append(tableId).append(" = ?");

        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql.toString(), id);
    }

}
