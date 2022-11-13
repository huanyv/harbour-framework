# web框架

## 0. 使用

* web.xml

```xml

<servlet>
	<servlet-name>routerServlet</servlet-name>
	<servlet-class>top.huanyv.webmvc.servlet.RouterServlet</servlet-class>
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
<listener-class>top.huanyv.webmvc.servlet.WebApplicationListener</listener-class>
</listener>
```

## 1. 路由注册

### 1.1 注解方法式

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

### 1.2 接口函数式

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
    public SqlContextFactoryBean sqlContextFactoryBean() {
        // 加载配置
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        simpleDataSource.setDriverClassName(Driver.class.getName());
        simpleDataSource.setUsername("root");
        simpleDataSource.setPassword("2233");

        jdbcConfigurer.setDataSource(simpleDataSource);
        jdbcConfigurer.setScanPackages("com.book");

        return new SqlContextFactoryBean();
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

