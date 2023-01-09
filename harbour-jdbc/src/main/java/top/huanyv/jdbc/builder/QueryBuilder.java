package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;

import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 17:14
 */
public class QueryBuilder<T> {
    protected SqlBuilder<T> sqlBuilder;

    public QueryBuilder() {
        this.sqlBuilder = new SqlBuilder<>();
    }
    public QueryBuilder(SqlBuilder<T> sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public String sql() {
        return sqlBuilder.sql();
    }

    public QueryBuilder<T> append(String sql) {
        this.sqlBuilder.append(sql);
        return this;
    }

    /**
     * 拼接sql
     * @param sql sql语句
     * @param args 参数列表
     * @return this
     */
    public QueryBuilder<T> append(String sql, Object... args) {
        this.sqlBuilder.append(sql);
        setArguments(args);
        return this;
    }

    /**
     * 动态拼接sql
     * @param condition 条件，为false不拼接
     * @param sql sql语句
     * @param args 参数列表
     * @return this
     */
    public QueryBuilder<T> append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }

    /**
     * 获取表名，默认是类名首字母小写，有@TableName注解，用注解的value属性
     * @return string
     */
    public String getTableName() {
        return this.sqlBuilder.getTableName();
    }

    /**
     * 设置参数
     * @param args 数据列表
     */
    public void setArguments(Object... args) {
        this.sqlBuilder.setArguments(args);
    }

    public Object[] getArguments() {
        return this.sqlBuilder.getArguments().toArray();
    }

    /**
     * 获取sql语句的最后一个关键字
     * @return string
     */
    public String endKeyWord() {
        return this.sqlBuilder.getSqlList().get(this.sqlBuilder.getSqlList().size() - 1);
    }


    /**
     * 查询第一行的数据
     * @return 对象
     */
    public T selectRow() {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.selectRow(this.sqlBuilder.tableClass, sql(), getArguments());
    }

    /**
     * 查询出多行多列的数据
     * @return list
     */
    public List<T> selectList() {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.selectList(this.sqlBuilder.tableClass, sql(), getArguments());
    }

    /**
     * 查询某一行，某列的数据
     * @param clazz 数据类型
     * @param <E> 数据类型
     * @return 数据
     */
    public <E> E selectValue(Class<E> clazz) {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return (E) sqlContext.selectValue(sql(), getArguments());
    }

    /**
     * 查询数据条数
     * @return long
     */
    public Long count() {
        return selectValue(Long.class);
    }

    /**
     * 查询分页信息
     * @param pageNum 当前页
     * @param pageSize 每页长度
     * @return page对象
     */
    public Page<T> page(int pageNum, int pageSize) {
        Page<T> page = new Page<>(pageNum, pageSize);
        SqlContextFactory.getSqlContext().selectPage(page, this.sqlBuilder.tableClass, sql(), getArguments());
        return page;
    }

    /**
     * 增删改数据
     * @return 改动成功的条数，> 0 表示修改成功
     */
    public int update() {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql(), getArguments());
    }

}
