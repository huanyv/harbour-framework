package top.huanyv.jdbc.support;

import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.bean.aop.JoinPoint;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.bean.utils.ClassUtil;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * 事务管理器
 */
public class TransactionAop implements AspectAdvice {

    @Override
    public Object aroundAdvice(JoinPoint point)  {

        SqlContext sqlContext = SqlContextFactory.getSqlContext();

        Object result = null;
        try {
            // 开启事务
            sqlContext.beginTransaction();

            result = point.invoke();

            // 事务提交
            sqlContext.commit();
        } catch (Exception e) {
            // 事务回滚
            sqlContext.rollback();

            // 处理事务返回值
            Class<?> returnType = point.getMethod().getReturnType();
            // 如果是数字类型
            if (ClassUtil.isNumberType(returnType)) {
                result = 0;
            } else if (char.class.equals(returnType)){
                result = ' ';
            } else if (boolean.class.equals(returnType)) {
                result = false;
            } else {
                result = null;
            }

            Exception targetException = e;
            if (e instanceof InvocationTargetException) {
                targetException = ReflectUtil.getTargetException((InvocationTargetException) e);
            }
            targetException.printStackTrace();
        } finally {
            return result;
        }
    }
}
