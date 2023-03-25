package top.huanyv.webmvc.security;

/**
 * @author huanyv
 * @date 2023/3/23 20:18
 */
public class SubjectHolder {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setSubject(User user) {
        threadLocal.set(user);
    }

    public static User getSubject() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
