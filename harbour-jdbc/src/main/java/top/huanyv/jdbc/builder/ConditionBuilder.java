package top.huanyv.jdbc.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/1 15:07
 */
public class ConditionBuilder implements Serializable {

    private static final long serialVersionUID = 4702903648636043900L;

    private final StringBuilder sqlCondition;

    private final List<Object> sqlArgs;

    public ConditionBuilder() {
        this.sqlCondition = new StringBuilder();
        this.sqlArgs = new ArrayList<>();
    }

    public ConditionBuilder append(String str, Object... args) {
        if (str != null && str.length() == 0) {
            return this;
        }
        if (!isEmpty()) {
            sqlCondition.append(" ");
        }
        sqlCondition.append(str);
        if (args != null && args.length > 0) {
            sqlArgs.addAll(Arrays.asList(args));
        }
        return this;
    }

    public ConditionBuilder append(boolean condition, String str, Object... args) {
        if (condition) {
            append(str, args);
        }
        return this;
    }


    public ConditionBuilder and(String str, Object... args) {
        append(!isEmpty(), "and");
        append(str, args);
        return this;
    }

    public ConditionBuilder and(boolean condition, String str, Object... args) {
        if (condition) {
            and(str, args);
        }
        return this;
    }


    public ConditionBuilder or(String str, Object... args) {
        append(!isEmpty(), "or");
        append(str, args);
        return this;
    }

    public ConditionBuilder or(boolean condition, String str, Object... args) {
        if (condition) {
            or(str, args);
        }
        return this;
    }

    public boolean isEmpty() {
        return this.sqlCondition.length() == 0;
    }

    public String getSql() {
        return this.sqlCondition.toString();
    }

    public Object[] getArgs() {
        return sqlArgs.toArray();
    }

    @Override
    public String toString() {
        return this.sqlCondition.toString();
    }

}
