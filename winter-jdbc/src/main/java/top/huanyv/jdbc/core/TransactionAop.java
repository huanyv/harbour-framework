package top.huanyv.jdbc.core;

import top.huanyv.ioc.aop.AdvicePoint;
import top.huanyv.ioc.aop.AspectAdvice;

/**
 * 事务管理器
 */
public class TransactionAop implements AspectAdvice {

    @Override
    public Object aroundAdvice(AdvicePoint point)  {

        SqlContext sqlContext = SqlContextFactory.getSqlContext();

        Object result = null;
        try {
            // 开启事务
            sqlContext.beginTransaction();

            result = point.invoke();

            // 事务提交
            sqlContext.commit();
        } catch (Exception throwables) {
            // 事务回滚
            sqlContext.rollback();
            throwables.printStackTrace();
            return 0;
        }
        return result;
    }
}
