package top.huanyv.jdbc.support;

import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.jdbc.core.datasource.DataSourceKeyHolder;

/**
 * @author huanyv
 * @date 2023/1/9 14:45
 */
public abstract class AbstractDataSourceAop implements AspectAdvice {

    @Override
    public void beforeAdvice(Object[] args) {
        DataSourceKeyHolder.set(getDataSourceKey());
    }

    public abstract String getDataSourceKey();
}
