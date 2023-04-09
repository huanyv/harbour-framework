package top.huanyv.webmvc.security;

import top.huanyv.webmvc.utils.ServletHolder;

import javax.servlet.http.HttpSession;

/**
 * @author huanyv
 * @date 2023/4/9 10:56
 */
public class SessionStorageStrategy implements StorageStrategy {

    public static final String SUBJECT_SESSION_KEY = "harbour:login:user";

    @Override
    public void setSubject(User user) {
        HttpSession session = getSession();
        session.setAttribute(SUBJECT_SESSION_KEY, user);
    }

    @Override
    public User getSubject() {
        HttpSession session = getSession();
        return (User) session.getAttribute(SUBJECT_SESSION_KEY);
    }

    @Override
    public void clear() {
        getSession().removeAttribute(SUBJECT_SESSION_KEY);
    }

    public HttpSession getSession() {
        return ServletHolder.getRequest().getSession();
    }
}
