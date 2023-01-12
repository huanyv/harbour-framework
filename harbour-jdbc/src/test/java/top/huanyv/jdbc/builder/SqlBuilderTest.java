package top.huanyv.jdbc.builder;

import org.junit.Test;

import static org.junit.Assert.*;

public class SqlBuilderTest {

    @Test
    public void join() {
        SqlBuilder sb = new SqlBuilder("update table")
                .join(", ", "set", joiner -> joiner
                        .append("name = ?", "lisi")
                        .append(false, "age = ?", 19)
                )
                .condition("where", c -> c.append("id = ?", 1));

        System.out.println(sb);
    }
}