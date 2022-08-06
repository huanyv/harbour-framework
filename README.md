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


------------------------------------------------------------------------------------------------------------------

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

------------------------------------------------------------------------------------------------------------------

# web框架

## 0. 使用

* web.xml

```xml
<servlet>
	<servlet-name>routerServlet</servlet-name>
	<servlet-class>top.huanyv.web.servlet.RouterServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>routerServlet</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>

<context-param>
	<param-name>ScanPackages</param-name>
	<param-value>org.example</param-value>
</context-param>
<listener>
	<listener-class>top.huanyv.web.servlet.WebApplicationListener</listener-class>
</listener>
```

## 1. 路由注册

### 1.1 注解式

* `@Route`所有请求
* `@Get`get请求

```java
@Component
@Route("/test") // 必须有
public class HelloController {

    @Autowired
    private UserService userService;

    @Route("/")
    public void hello(HttpRequest req, HttpResponse resp) throws IOException {
        System.out.println("userService.getUserById(1) = " + userService.getUserById(1));
        resp.html("<h1>hello</h1>");
    }

    @Route("/admin")
    public void admin(HttpRequest req, HttpResponse resp) throws IOException {
        String s = null;
        if (s.equals("")) {
            System.out.println(11);
        }
        resp.html("admin");
    }

    @Get("/get/{id}/{name}")
    public void gettest(HttpRequest req, HttpResponse resp) throws IOException {
        System.out.println("req.pathVar(\"id\") = " + req.pathVar("id"));
        System.out.println("req.pathVar(\"name\") = " + req.pathVar("name"));
        resp.html("<h1>get</h1>");
    }

}
```

### 1.2 接口式

```java
@Component
public class ChainController implements RouteRegistry {

    @Autowired
    private UserService userService;

    @Override
    public void run(Routing app) {

        app.get("/say", (req, resp) -> {
            System.out.println("userService.getUserById(1) = " + userService.getUserById(1));
            resp.json(userService.getUserById(1));
        }).post("/user/{id}/{name}", (req, resp) -> {
            System.out.println("req.pathVar(\"id\") = " + req.pathVar("id"));
            System.out.println("req.pathVar(\"name\") = " + req.pathVar("name"));
        });

    }
}
```

## 2. restful

```java
@Get("/get/{id}/{name}")
public void gettest(HttpRequest req, HttpResponse resp) throws IOException {
    System.out.println("req.pathVar(\"id\") = " + req.pathVar("id"));
    System.out.println("req.pathVar(\"name\") = " + req.pathVar("name"));
    resp.html("<h1>get</h1>");
}
```

## 3. 配置

```java
@Component
@Configuration
public class WebConfig implements WebConfigurer {

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.add("/", "index");
    }

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // 设置允许跨域请求的域名
            .allowedOriginPatterns("*")
            // 是否允许cookie
            .allowCredentials(true)
            // 设置允许的请求方式
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            // 设置允许的header属性
            .allowedHeaders("*")
            // 跨域允许时间
            .maxAge(3600L);
    }

    @Bean
    public DataSource dataSource() {
        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        simpleDataSource.setDriverClassName(Driver.class.getName());
        simpleDataSource.setUsername("root");
        simpleDataSource.setPassword("2233");
        return simpleDataSource;
    }

    @Bean
    public MapperScanner mapperScanner() {
        return new MapperScanner("com.book");
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        return new SqlSessionFactoryBean();
    }
}
```

### 3.1 跨域配置

* 在配置类中配置
* 使用默认的规则`registry.addMapping("/static/**").defaultRule();`

```java
@Component
@Configuration
public class WebConfig implements WebConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // 设置允许跨域请求的域名
            .allowedOriginPatterns("*")
            // 是否允许cookie
            .allowCredentials(true)
            // 设置允许的请求方式
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            // 设置允许的header属性
            .allowedHeaders("*")
            // 跨域允许时间
            .maxAge(3600L);
    }
}
```


## 4. 页面资源

* 可以使用thymeleaf视图解析器
* 配置类可以配置视图控制器
* 静态资源也可以配置

```java
@Configuration
@Component
public class Webconfig implements WebConfigurer {

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.add("/view", "view");
    }

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
	    registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/")
	    	.addResourceLocations("C:\\Users\\admin\\Desktop\\demo\\")
    }

    @Bean
    public ViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        return thymeleafViewResolver;
    }

}
```

## 5. 路由守卫

```java
public class Guard4 implements NavigationGuard {
    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp) {
        System.out.println("guard4-before");
        return true;
    }

    @Override
    public void afterEach(HttpRequest req, HttpResponse resp) {
        System.out.println("guard4-after");
    }

}
```

### 5.1 配置类

```java
@Override
public void configNavigationRegistry(NavigationGuardRegistry registry) {
    registry.addNavigationGuard(new Guard4()).addUrlPattern("/**")
            .excludeUrlPattern("/statics/**")
            .setOrder(-1);
}
```

### 5.2 注解组件注册

```java
@Component
@Guard(value = {"/**"}, exclude = {"/static/**", "/statics/**"}, order = 1)
public class Guard1 implements NavigationGuard {
    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp) {
        System.out.println("guard1-before");
        return true;
    }

    @Override
    public void afterEach(HttpRequest req, HttpResponse resp) {
        System.out.println("guard1-after");
    }
}
```

## 6. 异常处理器

* 实现`ExceptionHandler`接口，只能有一个异常处理器类

```java
@Component
public class GlobalException implements ExceptionHandler {

    @ExceptionPoint(NullPointerException.class)
    public void handle1(HttpRequest request, HttpResponse response, NullPointerException e) throws Exception{
        System.out.println("空指针了");
        response.html("空指针");
    }


    @ExceptionPoint(IllegalArgumentException.class)
    public void illegal(HttpRequest req, HttpResponse resp, IllegalArgumentException e) {
        System.out.println("异常");
    }
}
```

## 7. 其它

* 重新封装了request与response对象
* 保留原来api，增加了几个新的方法，简化部分操作
* request
	* `forward(String path)`请求转发
	* `view(String name)`转发到视图
	* `body()`获取请求体
	* `pathVar(String name)`获取restful路径变量
	* `Map<String, String> uploadFile(File file)`上传文件，file是文件夹，返回非文件的参数
* response
	* `redirect(String location)`重定向
	* `html(String content)`响应html
	* `text(String content)`响普通文件
	* `json(Object content)`响应json，非字符串对象会自动转成json格式
	* `xml(String content)`响应xml
	* `file(File file)`下载文件
* `getOriginal()`获取原生的request、response

------------------------------------------------------------------------------------------------------------------

# boot-内嵌tomcat封装

* 不支持jsp
* `templates`文件夹为视图前缀
* `/static/**`静态资源为classpath:static

## 使用

```java
public class MainTest {
    @Test
    public void test01() {
        Winter app = Winter.use();

        app.get("/admin", (req, resp) -> {
            resp.html("<h1>admin</h1>");
        });
        app.get("/", (req, resp) -> {
            req.view("index");
        });

        app.start(MainTest.class, new String[]{});

    }
}
```

```java
public class BookmanagementApplication {
    public static void main(String[] args) {
        WinterApplication.run(BookmanagementApplication.class, args);
    }
}

```

## 配置文件

```
server.port=8090
server.context-path=/book
```

## 命令行参数

* `--server.port=8080`端口号
* `--app.env=prod`配置文件`application-prod.properties`

## 自定义banner

* classpath下新建banner.txt


## 打包运行

* 内嵌tomcat，打jar包即可
* 引入`maven-assembly-plugin`插件，并指名主方法类
* `java -jar `有`jar-with-dependencies`的jar包

```xml
<packaging>jar</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <dependency>
            <groupId>top.huanyv</groupId>
            <artifactId>winter-boot</artifactId>
            <version>1.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <mainClass>主方法类</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>assembly</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```