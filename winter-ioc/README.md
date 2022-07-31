# 简单的IOC容器

## 1. 组件注册

* 在类上使用`@Component`注解，value属性指定名称

## 2. 依赖注入

* `@Autowired`按类型注入
* `@Qualifier`按名称注入

```java
@Component
public class BookService {

    @Autowired
    private UserService userService;

	// .......
}
```

## 3. 实例Bean注入

* 类上使用`@Configuration`注解
* 方法使用`@Bean`注解，方法返回类型为bean的类型，方法名为bean名称

```java
@Configuration
public class CustomConfig {

    @Bean
    public String string2() {
        return "abcdef";
    }

}
```

## 4. 使用

```java
AnnotationConfigApplicationContext app
        = new AnnotationConfigApplicationContext("com.example");

UserService userService = (UserService) app.getBean("userService");
userService.getUser();

BookService bookService = app.getBean(BookService.class);
System.out.println("bookService = " + bookService.getUserService());
```