package top.huanyv.jdbc.util;

import org.junit.Test;
import top.huanyv.jdbc.annotation.TableName;

import static org.junit.Assert.*;

public class SqlUtilTest {

    @Test
    public void generateInsert() {
        String sql = SqlUtil.generateInsert(SysUser.class, false);
        System.out.println("sql = " + sql);
    }


    @Test
    public void generateUpdate() {
        String sql = SqlUtil.generateUpdate(SysUser.class, false);
        System.out.println("sql = " + sql);
    }

    @Test
    public void generateUpdateCode() {
        String code = SqlUtil.generateUpdateDynamicCode(SysUser.class, "user",true);
        System.out.println(code);
    }
}


@TableName("sys_user")
class SysUser {
    private Long id;
    private String userName;
    private String nickName;
}