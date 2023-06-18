package top.huanyv.jdbc.builder;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SqlBuilderTest {

    @Test
    public void testJoin() {
        SqlBuilder sb = new SqlBuilder("update table")
                .join("set", ", ", joiner -> joiner
                        .append("name = ?", "lisi")
                        .append(false, "age = ?", 19)
                )
                .condition("where", c -> c.append("id = ?", 1));

        System.out.println(sb);
        System.out.println(sb.getSql());
        System.out.println(Arrays.toString(sb.getArgs()));
    }


    @Test
    public void testCondition() {
        SqlBuilder sb = new SqlBuilder("select * from t_user")
                .condition("where", c -> c
                        .append( "id = ?", 1)
                        .append("and password = ?", "123456")
                        .or("hha = ?", 2)
                        .and(true, "username = ?", "lisi")
                        .or(false, "sex = ?", "男")
                ).append("order by id").append(false, "desc");

        System.out.println(sb);
        System.out.println(sb.getSql());
        System.out.println(Arrays.toString(sb.getArgs()));
    }
}