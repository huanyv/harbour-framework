# jdbc - 简化JDBC操作

## 接口式调用

```java
@Mapper
public interface UserDao {

    @Select("select * from user where uid = ?")
    User getUserById(Integer id);


    @Select("select * from user")
    List<User> getUser();


    @Select("select count(*) from user")
    long getUserCount();

    @Select("select username from user where uid = ?")
    String getUserNameById(int id);

    @Update("update user set username = ? where uid = ?")
    int updateUsernameById(String username, Integer id);
}
```

## 调用

```
mapper.scan=top.huanyv.jdbc.core
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/temp?useSSL=false
username=root
password=123
```

```java
InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);

UserDao userDao = sqlSession.getMapper(UserDao.class);

System.out.println("userDao.getUserById(1) = " + userDao.getUserById(1));
```


## 事务

```java
public class TransactionAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(AdvicePoint point)  {
        Connection connection = ConnectionHolder.getCurConnection();
        // 关闭 winter-jdbc的自动关闭连接
        ConnectionHolder.setAutoClose(false);

        Object result = null;
        try {
        	// 开启事务
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

        return result;
    }
}

```