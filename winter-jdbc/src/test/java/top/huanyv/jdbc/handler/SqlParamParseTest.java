package top.huanyv.jdbc.handler;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParamParseTest extends TestCase {

    public void testGetSql() {
        String placeholder = "select * from user where password = #{password} and username = #{username}";

        User user = new User();
        user.setUid(6L);
        user.setUsername("admin");
        user.setPassword("abc123");
        user.setSex("女");
        user.setEmail("123@qq.com");

        SqlParamParser sqlParamParse = new SqlParamParser(placeholder, "zhangsan", "123", user);
        String sql = sqlParamParse.getSql();
        Object[] args = sqlParamParse.getArgs();
        System.out.println(sql);
        System.out.println("args = " + Arrays.toString(args));
    }

    public void testGetArgs() {

        String sql = "select * from user where username = ? and password = #{password}";

        Pattern pattern = Pattern.compile(SqlParamParser.PLACEHOLDER_REGEX);
        Matcher matcher = pattern.matcher(sql);
        System.out.println(matcher.find()); // 查找，找到 true  无 false
        System.out.println(matcher.matches()); // 整个匹配



    }
}