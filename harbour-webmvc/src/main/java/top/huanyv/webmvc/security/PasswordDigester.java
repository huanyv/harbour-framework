package top.huanyv.webmvc.security;

/**
 * 密码信息摘要器
 *
 * @author huanyv
 * @date 2023/3/22 21:13
 */
public interface PasswordDigester {

    String digest(String password);

}
