package top.huanyv.jdbc.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/1 15:07
 */
public class ConditionBuilder {

    private List<String> sqlCondition = new ArrayList<>();

    private List<Object> sqlArgs = new ArrayList<>();

    public ConditionBuilder append(String conditionStr, Object... args) {
        sqlCondition.add(conditionStr);
        sqlArgs.addAll(Arrays.asList(args));
        return this;
    }

    public ConditionBuilder append(boolean condition, String conditionStr, Object... args) {
        if (condition) {
            append(conditionStr, args);
        }
        return this;
    }

    public ConditionBuilder and(String conditionStr, Object... args) {
        if (!this.sqlCondition.isEmpty()) {
            sqlCondition.add("and");
        }
        append(conditionStr, args);
        return this;
    }

    public ConditionBuilder and(boolean condition, String conditionStr, Object... args) {
        if (condition) {
            and(conditionStr, args);
        }
        return this;
    }


    public ConditionBuilder or(String conditionStr, Object... args) {
        if (!this.sqlCondition.isEmpty()) {
            sqlCondition.add("or");
        }
        append(conditionStr, args);
        return this;
    }

    public ConditionBuilder or(boolean condition, String conditionStr, Object... args) {
        if (condition) {
            or(conditionStr, args);
        }
        return this;
    }

    public ConditionBuilder not(String conditionStr, Object... args) {
        if (!this.sqlCondition.isEmpty()) {
            sqlCondition.add("not");
        }
        append(conditionStr, args);
        return this;
    }

    public ConditionBuilder not(boolean condition, String conditionStr, Object... args) {
        if (condition) {
            not(conditionStr, args);
        }
        return this;
    }

    public boolean isEmpty() {
        return this.sqlCondition.isEmpty();
    }

    @Override
    public String toString() {
        return String.join(" ", sqlCondition);
    }

    public Object[] getArgs() {
        return sqlArgs.toArray();
    }
}
