package top.huanyv.webmvc.security;

import top.huanyv.webmvc.security.digest.BCryptPasswordDigester;
import top.huanyv.webmvc.security.digest.PasswordDigester;

import java.util.Objects;

/**
 * 身份认证器
 *
 * @author huanyv
 * @date 2023/3/22 21:12
 */
public class Authenticator {

    /**
     * 系统中的用户主体
     */
    private final User user;

    private final PasswordDigester digester;

    public Authenticator(User user) {
        this.digester = new BCryptPasswordDigester();
        this.user = user;
    }

    public Authenticator(User user, PasswordDigester digester) {
        this.user = user;
        this.digester = digester;
    }

    /**
     * 进行身份验证
     *
     * @param username 用户名
     * @param password 密码
     * @return boolean
     */
    public boolean authenticate(String username, String password) {
        // 如果用户名不一样
        if (!Objects.equals(username, this.user.getUsername())) {
            return false;
        }
        // 如果密码错误
        if (!this.digester.match(password, user.getPassword())) {
            return false;
        }
        return true;
    }

}
