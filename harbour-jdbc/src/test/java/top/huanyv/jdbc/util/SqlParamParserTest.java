package top.huanyv.jdbc.util;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;

public class SqlParamParserTest extends TestCase {

    public void testGenerateInsert() throws Throwable {
        String sql = SqlParamParser.generateInsert(User.class);
        System.out.println(sql);
    }

    public void testGenerateUpdate() throws Throwable {
        String sql = SqlParamParser.generateUpdate(User.class);
        System.out.println(sql);
    }
}