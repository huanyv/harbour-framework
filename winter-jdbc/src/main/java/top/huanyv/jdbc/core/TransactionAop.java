package top.huanyv.jdbc.core;

import top.huanyv.ioc.aop.AdvicePoint;
import top.huanyv.ioc.aop.AspectAdvice;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(AdvicePoint point)  {
        Connection connection = ConnectionHolder.getCurConnection();
        ConnectionHolder.setAutoClose(false);

        Object result = null;
        try {
            connection.setAutoCommit(false);

            result = point.invoke();

            connection.commit();
        } catch (Exception throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
            return 0;
        }

        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }
}
