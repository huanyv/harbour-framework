package top.huanyv.boot.core;

/**
 * web服务器
 *
 * @author huanyv
 * @date 2022/11/5 19:21
 */
public interface WebServer {

    /**
     * 服务启动方法
     *
     * @param mainClass 主类
     * @param args      arg游戏
     */
    void start(Class<?> mainClass, String[] args);

    /**
     * 服务停止
     */
    void stop();

}
