# 内嵌tomcat封装

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