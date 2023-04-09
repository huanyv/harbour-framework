package top.huanyv.webmvc.security;

import top.huanyv.webmvc.utils.ServletHolder;

import javax.servlet.http.HttpSession;

/**
 * @author huanyv
 * @date 2023/4/9 10:56
 */
public class ThreadLocalStorageStrategy implements StorageStrategy {

    public static final ThreadLocal<User> subjectLocal = new ThreadLocal<>();

    @Override
    public void setSubject(User user) {
        subjectLocal.set(user);
    }

    @Override
    public User getSubject() {
        return subjectLocal.get();
    }

    @Override
    public void clear() {
        subjectLocal.remove();
    }

}
