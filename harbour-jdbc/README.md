# jdbc - 简化JDBC操作

## 使用

* 配置文件：`jdbc.properties`

```
mapper.scan=top.huanyv.jdbc.core
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/temp?useSSL=false
username=root
password=2233
```

```java
public void testGetSqlContext() throws Exception {
	// 配置
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
    JdbcConfigurer.create(inputStream);

    // 获取 SqlContext 实例
    SqlContext sqlContext = SqlContextFactory.getSqlContext();
    try {
    	// 开启事务
        sqlContext.beginTransaction();
        int update = sqlContext.update("update user set username = ? where uid = ?", "lisi", 7);
        System.out.println("update = " + update);
		// int i = 10 / 0;
        int delete = sqlContext.update("delete from user where uid = ?", 9);
        System.out.println("delete = " + delete);
		// 事务提交
        sqlContext.commit();
    } catch (Exception e) {
     	// 事务回滚
        sqlContext.rollback();
        e.printStackTrace();
    }
}

```

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

```java
// 加载配置
InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
JdbcConfigurer.create(inputStream);

SqlContext sqlContext = SqlContextFactory.getSqlContext();

UserDao userDao = sqlContext.getDao(UserDao.class);
List<User> users = userDao.getUser();
users.stream().forEach(System.out::println);
```

## 事务

```java
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
```