package top.huanyv.start.server;

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
     */
    void start();

    /**
     * 服务停止
     */
    void stop();

}
