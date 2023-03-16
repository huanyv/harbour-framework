package top.huanyv.jdbc.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * sql构造器，构造动态SQL使用，需要调用getSql()、getArgs()方法
 * 获取SQL语句和参数
 *
 * <pre>{@code
 * SqlBuilder sb = new SqlBuilder("select * from t_book")
 *     .condition("where", c -> c
 *         .append(StringUtil.hasText(bname), "bname like ?", "%" + bname + "%")
 *     );
 * }
 * </pre>
 *
 * <pre>{@code
 *   SqlBuilder sb = new SqlBuilder("update t_book")
 *       .join("set", ", ", j -> j
 *           .append(StringUtil.hasText(book.getBname()), "bname = ?", book.getBname())
 *           .append("author = ?", book.getAuthor())
 *           .append("pubcomp = ?", book.getPubcomp())
 *           .append("pubdate = ?", book.getPubdate())
 *           .append("bcount = ?", book.getBcount())
 *           .append("price = ?", book.getPrice())
 *       )
 *       .condition("where", condition -> condition.append("id = ?", book.getId()));
 * }
 * </pre>
 *
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
        if (str != null && str.length() == 0) {
            return this;
        }
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

    public SqlBuilder join(String separator, Consumer<SqlJoiner> joiner) {
        return join("", "", separator, joiner);
    }

    public SqlBuilder join(String prefix, String separator, Consumer<SqlJoiner> joiner) {
        return join(prefix, separator, "", joiner);
    }

    /**
     * 以指定前缀、后缀、分割符号连接SQL字符串，适合 update 操作
     * <pre>{@code
     *   SqlBuilder sb = new SqlBuilder("update t_book")
     *       .join("set", ", ", j -> j
     *           .append(StringUtil.hasText(book.getBname()), "bname = ?", book.getBname())
     *           .append("author = ?", book.getAuthor())
     *           .append("pubcomp = ?", book.getPubcomp())
     *           .append("pubdate = ?", book.getPubdate())
     *           .append("bcount = ?", book.getBcount())
     *           .append("price = ?", book.getPrice())
     *       )
     *       .condition("where", condition -> condition.append("id = ?", book.getId()));
     * }
     * </pre>
     *
     * @param prefix    前缀
     * @param suffix    后缀
     * @param separator 分隔符
     * @param j         连接器
     * @return {@link SqlBuilder}
     */
    public SqlBuilder join(String prefix, String suffix, String separator, Consumer<SqlJoiner> j) {
        SqlJoiner joiner = new SqlJoiner(separator);
        j.accept(joiner);
        if (!joiner.isEmpty()) {
            append(prefix);
            append(joiner.getSql(), joiner.getArgs());
            append(suffix);
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
