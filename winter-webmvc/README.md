# 内嵌tomcat封装

## 1. 快速使用

* 此项目根目录执行`mvn install`
* 新建项目，引入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>top.huanyv</groupId>
            <artifactId>winter</artifactId>
            <version>2.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>
    </dependencies>
```

* main方法

```java
public class MainApplication {
    public static void main(String[] args) {
        Winter app = Winter.use();
        // GET请求
        app.get("/hello", (req, resp) -> {
            resp.html("<h1>Hello World!</h1>");
        });
        // POST请求
        app.post("/user", (req, resp) -> {
            resp.json("添加成功");
        });
        // GET、POST、PUT、DELETE，请求均可
        app.request("/haha", (req, resp) -> {
            resp.html("<h1>haha</h1>");
        });
        // filter过滤器
        app.filter("/*", (req, resp, chain) -> {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            chain.doFilter(req.getOriginal(), resp.getOriginal());
        });
        // 最后一步，启动
        app.start();
    }
}
```

## 2. restful

```java
app.get("/user/{id}/{name}", (req, resp) -> {
    String id = req.pathVar("id");
    String name = req.pathVar("name");
    resp.json("获取成功：用户" + id + ":" + name);
});

app.post("/user", (req, resp) -> {
    resp.json("添加成功");
});

app.delete("/user/{id}", (req, resp) -> {
    String id = req.pathVar("id");
    resp.json("用户" + id + "删除成功");
});
app.put("/user", (req, resp) -> {
    String id = req.pathVar("id");
    resp.json("用户修改成功");
});
```

## 3. 配置

* 继承`WebConfiguration`接口
* 调用`setConfig`方法

```java
app.setConfig(new WebConfiguration() {
    @Override
    public int getServerPort() {
        return 8090;
    }

    @Override
    public String getServerContext() {
        return "/test";
    }
});	
```

* 在`app.properties`，文件下写配置，配置项在`GlobalConfig`类下
* 配置类 > 配置文件


## 4. 页面资源

* 使用thymeleaf为视图引擎，默认在`templates`文件夹下
* 静态文件默认在`static`文件夹下
* 均是类路径
* 添加视图：

```java
app.addView("/", "index");
```

## 5. 分层

* 实现`ControllerRunner`接口，重写方法
* 加上`@Controller`注解
* 里面的代码只会执行一次

```java
@Controller
public class UserController implements ControllerRunner {
    @Override
    public void run(Winter app, UrlConver urlConver) {
        app.setConfig(new WebConfiguration() {
            @Override
            public int getServerPort() {
                return 8090;
            }

            @Override
            public String getServerContext() {
                return "/test";
            }
        });
        app.get("/admin", (req, resp) -> {
            resp.html("你好哈哈");
        });
        app.addView("/", "index");
    }
}
```

```java
public class MainApplication {
    public static void main(String[] args) {
        WinterApplication.run(MainApplication.class, args);
    }
}
```

## 6. 其它

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

## 7. 打包运行

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
            <groupId>top.huanyv</groupId>
            <artifactId>winter</artifactId>
            <version>2.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
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