package top.huanyv.jdbc.builder;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.huanyv.jdbc.core.ConnectionHolder;
import top.huanyv.jdbc.core.Page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 17:14
 */
public class QueryBuilder<T> {
    protected SqlBuilder<T> sqlBuilder;
    private QueryRunner queryRunner = new QueryRunner();

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
        append(sql);
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
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new BeanHandler<T>(this.sqlBuilder.tableClass), getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionHolder.autoClose();
        }
        return null;
    }

    /**
     * 查询出多行多列的数据
     * @return list
     */
    public List<T> selectList() {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new BeanListHandler<T>(this.sqlBuilder.tableClass), getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionHolder.autoClose();
        }
        return null;
    }

    /**
     * 查询某一行，某列的数据
     * @param clazz 数据类型
     * @param <E> 数据类型
     * @return 数据
     */
    public <E> E selectOne(Class<E> clazz) {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new ScalarHandler<>(), getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionHolder.autoClose();
        }
        return null;
    }

    /**
     * 查询数据条数
     * @return long
     */
    public Long count() {
        return selectOne(Long.class);
    }

    /**
     * 查询分页信息
     * @param pageNum 当前页
     * @param pageSize 每页长度
     * @return page对象
     */
    public Page<T> page(int pageNum, int pageSize) {
        Page<T> page = new Page<>();
        Long count = new Select("count(*)").from(this.sqlBuilder.tableClass).count();
        int pages = (int) (count / pageSize);
        if (count % pageSize > 0) {
            pages++;
        }
        int prePage = pageNum - 1;
        int nextPage = pageNum + 1;
        if (pageNum < 1) {
            pageNum = 1;
            prePage = 1;
            nextPage = pageNum + 1;
        } else if (pageNum > pages) {
            pageNum = pages;
            nextPage = pages;
            prePage = pageNum - 1;
        }
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal(count);
        page.setPages(pages);
        page.setPrePage(prePage);
        page.setNextPage(nextPage);

        int start = (pageNum - 1) * pageSize;
        int len = pageSize;

        List<T> data = append("limit").append(String.valueOf(start)).append(",").append(String.valueOf(len)).selectList();

        page.setData(data);
        page.setSize(data.size());

        return page;
    }

    /**
     * 增删改数据
     * @return 改动成功的条数，> 0 表示修改成功
     */
    public int update() {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.update(connection, sql(), getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ConnectionHolder.autoClose();
        }
        return 0;
    }


}
