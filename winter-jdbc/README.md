# 简化JDBC操作

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