# Harbour-framework

## 开始

`Harbour` 是一个追求简约、轻量的 Web 框架，集IOC、AOP、MVC、ORM于一体，让 `JavaWeb` 开发如虎添翼，在轻量与灵活性上同时兼顾。 在使用方式上提供与SpringBoot完全相同的注解和设计理念，学习转换零成本 。
如果你喜欢尝试有趣的事物，相信你会爱上它。**看完本文后请看详细文档：<https://huanyv.github.io>**
如果觉得这个项目不错可以 [star](https://github.com) 支持或者 [捐赠](https://github.com) 它

* 简洁的：框架设计简单,容易理解,不依赖于更多第三方库。
* 优雅的：`Harbour` 支持 REST 风格路由接口, 提供lambda函数路由。
* 易部署：支持 `maven` 打成 `jar` 包直接运行

## 功能特性

- [x] 轻量级MVC框架，不依赖更多的库
- [x] 模块化设计，各个模块之间可独立使用
- [x] 源码轻量，学习简单
- [x] Restful风格路由设计
- [x] 模板引擎支持，视图开发更灵活
- [x] 运行 `JAR` 包即可开启 web 服务
- [x] 流式API风格
- [x] 支持 webjars 资源
- [x] 内置多种常用功能
- [x] Jdk8 + Servlet4

## 快速上手

注意：本项目依赖了自己写的一个工具类 [java-utils](https://gitee.com/huanyv/java-utils) ，您需要先安装此工具类方可继续

安装依赖

执行`install.bat`脚本，双击即可，等待全部success

创建一个Maven项目，添加依赖

```xml
<dependency>
    <groupId>top.huanyv</groupId>
    <artifactId>harbour-start-web</artifactId>
    <version>1.0</version>
</dependency>
```

创建一个类，编写一个Main函数

```java
public class MainApplication {
    public static void main(String[] args) {
        Harbour.use().get("/hello", (req, resp)-> resp.html("<h1>Hello World!</h1>")).run(MainApplication.class);
    }
}
```

如果你使用过SpringBoot的话，它与其使用方式是相同的，将主方法类作为根包，即所有的其它包与类，都和主方法类在同一包下