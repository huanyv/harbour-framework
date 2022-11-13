package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.jdbc.util.BaseDaoUtil;
import top.huanyv.tools.utils.Assert;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author huanyv
 * @date 2022/8/31 19:58
 */
public interface BaseDao<T> {

    /**
     * 查询所有
     * @return list
     */
    default List<T> selectAll() {
        Class<?> beanType = BaseDaoUtil.getType(this.getClass());
        Assert.notNull(beanType, "BaseDao not set <generic>!");

        List objects = new Select().from(beanType).selectList();
        return objects;
    }

    /**
     * 根据ID查询一个对象
     * @param id id
     * @return T
     */
    default T selectById(Number id) {
        Class<?> beanType = BaseDaoUtil.getType(this.getClass());
        Assert.notNull(beanType, "BaseDao not set <generic>!");

        String tableId = BaseDaoUtil.getTableId(beanType);
        Object o = new Select().from(beanType).where(condition -> condition.append(tableId + " = ?", id))
                .selectRow();
        return (T) o;
    }

    /**
     * 根据对象实体，插入一条数据
     * @param t T
     * @return 插入成功的条数
     */
    default int insert(T t) {
        Class<?> clazz = t.getClass();
        // 参数
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("insert into ").append(BaseDaoUtil.getTableName(clazz));
        // (column1, column2, column3, .....)
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        // (?, ?, ?, ......)
        StringJoiner values = new StringJoiner(", ", "(", ")");

        String tableId = BaseDaoUtil.getTableId(clazz);
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object o = field.get(t);
                if (o != null && !field.getName().equals(tableId)) {
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

    /**
     * 根据实体对象，用ID更新一条数据。只更新属性不为 null 的字段，ID不可为空
     * @param t T
     * @return 更新成功的条数
     */
    default int updateById(T t) {
        try {
            Class<?> clazz = t.getClass();
            // 获取表名
            String tableName = BaseDaoUtil.getTableName(clazz);
            // sql语句
            StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
            // column1 = ?, column2 = ?, ......
            StringJoiner columns = new StringJoiner(", ");
            // 参数
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

    /**
     * 根据ID删除一条数据
     * @param id ID
     * @return 删除成功的条数
     */
    default int deleteById(Number id) {
        Class<?> type = BaseDaoUtil.getType(this.getClass());
        Assert.notNull(type, "BaseDao not set <generic>!");

        String tableId = BaseDaoUtil.getTableId(type);
        String tableName = BaseDaoUtil.getTableName(type);
        StringBuilder sql = new StringBuilder("delete from ").append(tableName)
                .append(" where ").append(tableId).append(" = ?");

        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql.toString(), id);
    }

}
