package top.huanyv.jdbc.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author huanyv
 * @date 2023/1/11 13:19
 */
public class SqlBuilder implements Serializable {

    private static final long serialVersionUID = -6781070557668986751L;

    private final StringBuilder sb;

    private final List<Object> sqlArgs;

    public SqlBuilder() {
        this.sb = new StringBuilder();
        this.sqlArgs = new ArrayList<>();
    }

    public SqlBuilder(String str) {
        this();
        this.sb.append(str);
    }

    public SqlBuilder append(String str, Object... args) {
        if (this.sb.length() != 0) {
            this.sb.append(" ");
        }
        this.sb.append(str);
        if (args != null && args.length > 0) {
            this.sqlArgs.addAll(Arrays.asList(args));
        }
        return this;
    }

    public SqlBuilder append(boolean condition, String str, Object... args) {
        if (condition) {
            append(str, args);
        }
        return this;
    }

    /**
     * 条件
     *
     * @param word 使用条件关键词，where having
     * @param c    c
     * @return {@link SqlBuilder}
     */
    public SqlBuilder condition(String word, Consumer<ConditionBuilder> c) {
        ConditionBuilder cb = new ConditionBuilder();
        c.accept(cb);
        if (!cb.isEmpty()) {
            append(word);
            append(cb.toString(), cb.getArgs());
        }
        return this;
    }

    public SqlBuilder with(String separator, Consumer<SqlJoiner> j) {
        return with(separator, "", "", j);
    }

    public SqlBuilder with(CharSequence separator, CharSequence prefix, Consumer<SqlJoiner> j) {
        return with(separator, prefix, "", j);
    }

    public SqlBuilder with(CharSequence separator, CharSequence prefix, CharSequence suffix, Consumer<SqlJoiner> j) {
        SqlJoiner joiner = new SqlJoiner(separator, prefix, suffix);
        j.accept(joiner);
        if (!joiner.isEmpty()) {
            append(joiner.getSql(), joiner.getArgs());
        }
        return this;
    }

    public String getSql() {
        return this.sb.toString();
    }

    public Object[] getArgs() {
        return this.sqlArgs.toArray();
    }

    @Override
    public String toString() {
        return "SQL:\t" + getSql() + "\nArgs:\t" + this.sqlArgs;
    }
}
