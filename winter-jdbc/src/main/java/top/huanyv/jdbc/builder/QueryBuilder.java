package top.huanyv.jdbc.builder;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.huanyv.jdbc.core.ConnectionHolder;
import top.huanyv.jdbc.core.Page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 17:14
 */
public class QueryBuilder<T> {
    protected SqlBuild<T> sqlBuilder;
    private QueryRunner queryRunner = new QueryRunner();

    public QueryBuilder() {
        this.sqlBuilder = new SqlBuild<>();
    }
    public QueryBuilder(SqlBuild<T> sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public String sql() {
        return sqlBuilder.sql();
    }

    public QueryBuilder<T> append(String sql) {
        this.sqlBuilder.append(sql);
        return this;
    }

    public QueryBuilder<T> append(String sql, Object... args) {
        append(sql);
        setArguments(args);
        return this;
    }
    public QueryBuilder<T> append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }

    public String getTableName() {
        return this.sqlBuilder.getTableName();
    }

    public void setArguments(Object... args) {
        this.sqlBuilder.setArguments(args);
    }

    public String endKeyWord() {
        return this.sqlBuilder.getSqlList().get(this.sqlBuilder.getSqlList().size() - 1);
    }


    public T selectOne() {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new BeanHandler<T>(this.sqlBuilder.table), this.sqlBuilder.getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<T> selectList() {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new BeanListHandler<T>(this.sqlBuilder.table), this.sqlBuilder.getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public <E> E selectScalar(Class<E> clazz) {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.query(connection, sql(), new ScalarHandler<>(), this.sqlBuilder.getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Long count() {
        return selectScalar(Long.class);
    }

    public Page<T> page(int pageNum, int pageSize) {
        Page<T> page = new Page<>();
        Long count = new Select("count(*)").from(this.sqlBuilder.table).count();
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

    public int update() {
        Connection connection = ConnectionHolder.getCurConnection();
        try {
            return queryRunner.update(connection, sql(), this.sqlBuilder.getArguments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


}
