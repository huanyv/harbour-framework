package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.tools.utils.StringUtil;

import java.util.Arrays;

public class SelectTest extends TestCase {

    public void testFrom() {
        QueryBuilder queryBuilder = new Select().from(User.class)
                .where(conditionBuilder -> conditionBuilder.and("uid = 1")
                        .and("username = admin"))
//                .and("uid = 1").and("username = 'admin'")
                .limit(10);
        String sql = queryBuilder.sql();
        System.out.println("queryBuilder.sqlBuilder.arguments = " + Arrays.toString(queryBuilder.getArguments()));
        System.out.println(sql);

    }

    public void test03() {
        Limit<User> sql = new Select().from(User.class)
                .where(condition -> condition
                        .append(StringUtil.hasText(""), "username = ?", "zhangsan")
                        .and(StringUtil.hasText(""), "password = ?", "1234556"))
                .orderBy().asc("age")
                .limit(1, 5);
        System.out.println("sql.sql() = " + sql.sql());
        System.out.println("sql.getArguments() = " + Arrays.toString(sql.getArguments()));
    }
}