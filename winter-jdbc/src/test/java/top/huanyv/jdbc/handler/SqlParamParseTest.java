package top.huanyv.jdbc.handler;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParamParseTest extends TestCase {

    public void testGetSql() {
        String placeholder = "select * from user where password = #{password} and username = #{username}  and height = #{shengao} and adress = #{addess}";

        User user = new User();
        user.setUid(6L);
        user.setUsername("admin");
        user.setPassword("abc123");
        user.setSex("女");
        user.setEmail("123@qq.com");

        Map mapping = new HashMap();
        mapping.put("address", "shanghai");
        mapping.put("shengao", 168);

        SqlParamParser sqlParamParser = new SqlParamParser();
        SqlParamParser.SqlAndArgs sqlAndArgs = sqlParamParser.parse(placeholder, user, mapping);

        System.out.println("sqlAndArgs.getSql() = " + sqlAndArgs.getSql());
        System.out.println("sqlAndArgs.getArgs() = " + Arrays.toString(sqlAndArgs.getArgs()));
    }

    public void testGetArgs() {
        String sql = "select * from user where username = ? and password = ?";

        Pattern pattern = Pattern.compile(SqlParamParser.PLACEHOLDER_REGEX);
        Matcher matcher = pattern.matcher(sql);
        System.out.println(matcher.find()); // 查找，找到 true  无 false
        System.out.println(matcher.matches()); //serv.encodserv.servl.encodeurl;

        SqlParamParser sqlParamParser = new SqlParamParser();
        SqlParamParser.SqlAndArgs sqlAndArgs = sqlParamParser.parse(sql, "andmin", 123);
        System.out.println(sqlAndArgs);
    }
}