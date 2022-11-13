# ioc-简单的IOC容器

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

## 5. AOP

* 实现`AspectAdvice`接口，为切面增强类
* 在需要增强的类或方法上使用`@Aop`注解，并指定刚刚的增强类
* 在三种通知，前置，后置、环绕

```java
public interface AspectAdvice {
    default void beforeAdvice(Object[] args) { }
    default void afterAdvice(Object[] args, Object result) { }
    default Object aroundAdvice(AdvicePoint point) throws InvocationTargetException, IllegalAccessException {
        return point.invoke();
    }
}
```

```java
public class LogAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(AdvicePoint point) throws InvocationTargetException, IllegalAccessException {
        Object[] args = point.getArgs();
        HttpRequest request = null;
        HttpResponse response = null;
        for (Object arg : args) {
            if (arg instanceof HttpRequest) {
                request = (HttpRequest) arg;
            }
            if (arg instanceof HttpResponse) {
                response = (HttpResponse) arg;
            }
        }

        Object result = point.invoke();

        Date now = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        System.out.println(nowTime + " -------------- " + "=======Start=======");
        System.out.println(nowTime + " -------------- " + "URL            : " + WebUtil.getRequestURI(request.getOriginal()));
        System.out.println(nowTime + " -------------- " + "HTTP Method    : " + request.getMethod());
        System.out.println(nowTime + " -------------- " + "Class Method   : " + point.getTarget().getClass().getName() + "." + point.getMethod().getName());
        System.out.println(nowTime + " -------------- " + "IP             : " + request.getRemoteAddr());
        System.out.println(nowTime + " -------------- " + "Request Args   : " + request.getParameterMap().entrySet()
                .stream().map(entry -> entry.getKey() + Arrays.toString(entry.getValue()))
                .collect(Collectors.toList()));
        System.out.println(nowTime + " -------------- " + "========End========");

        return result;
    }
}

```

```java
@Component
@Route("/admin/book")
@Aop(LogAop.class)
public class BookController {

	// .......

}
```
