# Harbour-framework

## 开始

`Harbour` 是一个追求简约、轻量的 Web 框架，集IOC、AOP、MVC、ORM于一体，让 `JavaWeb` 开发如虎添翼，在轻量与灵活性上同时兼顾。 在使用方式上提供与SpringBoot完全相同的注解和设计理念，学习转换零成本 。
如果你喜欢尝试有趣的事物，相信你会爱上它。
如果觉得这个项目不错可以 [star](https://github.com) 支持或者 [捐赠](https://github.com) 它

* 简洁的：框架设计简单,容易理解,不依赖于更多第三方库。
* 优雅的：`Harbour` 支持 REST 风格路由接口, 提供lambda函数路由。
* 易部署：支持 `maven` 打成 `jar` 包直接运行

## 功能特性

* [x] 轻量级MVC框架，不依赖更多的库
* [x] 模块化设计，各个模块之间可独立使用
* [x] 源码轻量，学习简单
* [x] Restful风格路由设计
* [x] 模板引擎支持，视图开发更灵活
* [x] 运行 `JAR` 包即可开启 web 服务
* [x] 流式API风格
* [x] 支持 webjars 资源
* [x] 内置多种常用功能
* [x] Jdk8 + Servlet4

## 快速上手

注意：本项目依赖了自己写的一个工具类 [java-utils](https://gitee.com/huanyv/java-utils) ，您需要先安装此工具类方可继续

安装依赖

执行`install.bat`脚本，双击即可，等待全部success

创建一个Maven项目，添加依赖

```xml
<dependency>
    <groupId>top.huanyv</groupId>
    <artifactId>harbour-start</artifactId>
    <version>1.0</version>
</dependency>
```

编写一个Main函数

```java
public static void main(String[] args) {
    Harbour.use().get("/hello", (req, resp) -> resp.html("Hello World!")).start(Main.class, args);
}
```



## 1. Bean组件管理

### 1.1 IOC

#### Bean注册

##### 1. 通过@Component注解

* 默认BeanName是类名首字母小写
* @Component("beanName")可以指定名字

```java
@Component
public class UserServiceImpl implements UserService {
	// ....
}

@Component("userSerivce")
public class UserServiceImpl implements UserService {
	// ....
}
```

##### 2. 通过@Bean注解

* 首先，要声名一个配置类，类上使用`@Configuration`注解
* 在方法上使用@Bean注解，方法名为Bean的名称

```java
@Configuration
public class Config {
    @Bean
    private MapperFactoryBean mapperFactoryBean() {
        return new MapperFactoryBean(UserDao.class);
    }
}
```

##### 3. 通过ApplicationContext的方法

* `register(Class<?>... componentClasses)`注册若干全Bean
* `registerBean(Class<?> beanClass, Object... constructorArgs)`注册一个Bean，指定构造参数，BeanName为类名小写
* `registerBean(String beanName, Class<?> beanClass, Object... constructorArgs)`
* `registerBeanDefinition(String beanName, BeanDefinition beanDefinition)`注册一个`BeanDefinition`
* 只有在调用`refresh()`才可创建实例

```java
ApplicationContext app = new AnnotationConfigApplicationContext("com.package");
app.registerBean("beanName", UserService.class);

// 刷新
app.refresh();
```

#### 多实例Bean

* 在标有`@component`或`@Bean`注解的类或方法上使用`@Scope`注解
* `@Scope("prototype")`或`@Scope(BeanDefinition.SCOPE_PROTOTYPE)`
* **注意：**多实例Bean不可循环依赖，否则报`BeanCurrentlyInCreationException`异常

```java
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserController {
	//.......
}
```

#### 依赖注入

* 在类的属性上使用`@Inject`注解，当指定名称时，按照BeanName注入，否则按照类型注入

```java
@Component
public class UserServiceImpl implements UserService {

    @Inject("userDao") // 名称注入
    private UserDao userDao;

	// .....

}

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserController {

    @Inject // 类型注入
    private UserService userService;

    // .....
}
```



### 1.2 AOP

* 创建切面，实现`AspectAdvice`接口，实现其中的方法
* 支持前置通知、后置通知、环绕通知，如想使用异常、最终通知，建议使用环绕通知`try{}catch{}finaly{}`捕获
* 在Bean上使用`@Aop`注解，指定切面，可以多个
* 可以在单个方法上使用，方法上的AOP和类上的AOP会组合

```java
public interface AspectAdvice {

    default void beforeAdvice(Object[] args) {

    }

    default void afterAdvice(Object[] args, Object result) {

    }

    default Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        // 环绕通知一定要调用这个方法才可继续执行
        return point.invoke();
    }

}
```

```java
public class LogAspect implements AspectAdvice {
    @Override
    public void beforeAdvice(Object[] args) {
        System.out.println(this.getClass() + "前置通知");
    }

    @Override
    public void afterAdvice(Object[] args, Object result) {
        System.out.println(this.getClass() + "后置通知");
    }

    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        System.out.println(this.getClass() + "环绕通知1");
        Object result = point.invoke();
        System.out.println(this.getClass() + "环绕通知2");
        return result;
    }
}

public class LogAspect2 implements AspectAdvice {
    @Override
    public void beforeAdvice(Object[] args) {
        System.out.println(this.getClass() + "前置通知");
    }

    @Override
    public void afterAdvice(Object[] args, Object result) {
        System.out.println(this.getClass() + "后置通知");
    }

    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        System.out.println(this.getClass() + "环绕通知1");
        Object result = point.invoke();
        System.out.println(this.getClass() + "环绕通知2");
        return result;
    }
}

@Component
@Aop({LogAspect.class, LogAspect2.class}) // 指定切面
public class AdminService {

    public User getUser() {
        System.out.println("方法执行");
        return new User(1, "admin", "123", "男", "111@qq.com");
    }

}

// 等效于上面
@Component
@Aop(LogAspect.class) // 指定切面
public class AdminService {

    @Aop(LogAspect2.class) // 指定切面
    public User getUser() {
        System.out.println("方法执行");
        return new User(1, "admin", "123", "男", "111@qq.com");
    }

}
```

> class top.huanyv.bean.test.aop.LogAspect环绕通知1
> class top.huanyv.bean.test.aop.LogAspect前置通知
> class top.huanyv.bean.test.aop.LogAspect2环绕通知1
> class top.huanyv.bean.test.aop.LogAspect2前置通知
> 方法执行
> class top.huanyv.bean.test.aop.LogAspect2后置通知
> class top.huanyv.bean.test.aop.LogAspect2环绕通知2
> class top.huanyv.bean.test.aop.LogAspect后置通知
> class top.huanyv.bean.test.aop.LogAspect环绕通知2

### 1.3 FactoryBean

* 工厂加工Bean
* 使用：实现`FactoryBean`接口，重写其中方法，注入到IOC容器中
* 取出的Bean为`getObject()`方法返回的实例，如果要取出原BeanFactory实例，使用`&BeanName`获得（类似C语言取地址符）

```java
public interface FactoryBean<T> {

    // Bean
    T getObject() throws Exception;

    // 类型
    Class<?> getObjectType();

    // 是否单例
    default boolean isSingleton() {
        return true;
    }
}
```

```java
// 对Bean进行代理加工（模拟Mybatis）
public class MapperFactoryBean implements FactoryBean {

    private Class<?> mapperInterface;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }
                // select、delete、insert、update
                return null;
            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    public MapperFactoryBean(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

}

@Configuration
public class Config {

    // 注入到IOC中
    @Bean
    private MapperFactoryBean mapperFactoryBean() {
        return new MapperFactoryBean(UserDao.class);
    }

}

public void testFactoryBean() {
    ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.factory");

    UserDao userDao = app.getBean(UserDao.class);
    System.out.println("userDao = " + userDao);
    System.out.println("userDao.getClass() = " + userDao.getClass());

    // 获取原BeanFactory
    System.out.println("app.getBean(\"&mapperFactoryBean\") = " + app.getBean("&mapperFactoryBean"));
}
```

> userDao = top.huanyv.bean.test.factory.MapperFactoryBean$1@512ddf17
> userDao.getClass() = class com.sun.proxy.$Proxy6
> app.getBean("&mapperFactoryBean") = top.huanyv.bean.test.factory.MapperFactoryBean@2c13da15

### 1.4 ApplicationContextWeave

* 应用初始化钩子
* 两种方式：
  1. 使用Java内置的SPI机制（无侵入式），在Resources目录下创建`META-INF/services`，创建`top.huanyv.bean.ioc.ApplicationContextWeave`文件，内容为接口实现类的全类名，一个一行
  2. 将`ApplicationContextWeave`的实现类注入到容器中（侵入式）

```java
public interface ApplicationContextWeave {

    // 执行顺序
    default int getOrder() {
        return 0;
    }

    default void createBeanInstanceAfter(ApplicationContext applicationContext) {
		// 如果在这个方法中使用了register方法注册单例Bean，一定要调用refresh方法，否则不会创建实例
    }

    default void populateBeanBefore(ApplicationContext applicationContext) {

    }

    default void populateBeanAfter(ApplicationContext applicationContext){

    }
}

```



```java
@Component
public class BeanWeave implements ApplicationContextWeave {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void createBeanInstanceAfter(ApplicationContext applicationContext) {
        System.out.println("创建实例后");
        applicationContext.registerBean(MapperFactoryBean.class, UserDao.class);
        applicationContext.refresh();
    }

    @Override
    public void populateBeanBefore(ApplicationContext applicationContext) {
        System.out.println("注入之前");
    }

    @Override
    public void populateBeanAfter(ApplicationContext applicationContext) {
        System.out.println("注入之后");
    }
}

public void testWeave() {
    ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.weave");
    UserDao userDao = app.getBean(UserDao.class);
    System.out.println(userDao);
}
```

>创建实例后
>注入之前
>注入之后
>top.huanyv.bean.test.factory.MapperFactoryBean$1@4aa8f0b4

