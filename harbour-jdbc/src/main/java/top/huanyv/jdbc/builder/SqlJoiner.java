package top.huanyv.jdbc.builder;

import top.huanyv.bean.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * @author huanyv
 * @date 2023/1/11 13:19
 */
public class SqlJoiner implements Serializable {

    private static final long serialVersionUID = -648629788701049928L;

    private final List<Object> sqlArgs;

    private final StringJoiner joiner;

    public SqlJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this.sqlArgs = new ArrayList<>();
        this.joiner = new StringJoiner(delimiter, prefix, suffix);
    }

    public SqlJoiner(CharSequence delimiter) {
        this(delimiter, "", "");
    }

    public SqlJoiner append(String str, Object... args) {
        if (str != null && str.length() == 0) {
            return this;
        }
        this.joiner.add(str);
        if (args != null && args.length > 0) {
            this.sqlArgs.addAll(Arrays.asList(args));
        }
        return this;
    }

    public SqlJoiner append(boolean condition, String str, Object... args) {
        if (condition) {
            append(str, args);
        }
        return this;
    }

    public boolean isEmpty() {
        return this.joiner.length() == 0;
    }

    public String getSql() {
        return this.joiner.toString();
    }

    public Object[] getArgs() {
        return this.sqlArgs.toArray();
    }

    @Override
    public String toString() {
        return "SQL:\t" + getSql() + "\nArgs:\t" + this.sqlArgs;
    }
}
