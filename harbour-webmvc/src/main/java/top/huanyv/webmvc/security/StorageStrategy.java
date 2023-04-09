package top.huanyv.webmvc.security;

/**
 * @author huanyv
 * @date 2023/4/9 10:53
 */
public interface StorageStrategy {

    String SESSION_STRATEGY = "session";
    String THREAD_LOCAL_STRATEGY = "thread-local";

    void setSubject(User user);

    User getSubject();

    void clear();
}
