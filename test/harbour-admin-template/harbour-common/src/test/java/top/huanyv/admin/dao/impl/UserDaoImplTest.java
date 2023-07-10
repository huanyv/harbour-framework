package top.huanyv.admin.dao.impl;


import com.mysql.jdbc.Driver;
import org.junit.Before;
import org.junit.Test;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.util.SqlParamParser;
import top.huanyv.jdbc.util.SqlUtil;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoImplTest {

    @Before
    public void load() {
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:mysql://localhost:3306/harbour?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("2233");
        JdbcConfigurer.create().setDataSource(dataSource);
    }

    @Test
    public void addUser() {
        String sql = SqlUtil.generateUpdate(SysUser.class);
        System.out.println("sql = " + sql);

        System.out.println(SqlUtil.generateUpdateDynamicCode(SysUser.class, "user",true));
    }

    @Test
    public void testGetPermissionsByUserId() {
        List<String> val = new UserDaoImpl().getPermissionsByUserId(1);
    }
}