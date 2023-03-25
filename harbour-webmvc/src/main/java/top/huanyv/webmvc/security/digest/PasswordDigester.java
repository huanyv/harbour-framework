package top.huanyv.webmvc.security.digest;

/**
 * 密码信息摘要器
 *
 * @author huanyv
 * @date 2023/3/22 21:13
 */
public interface PasswordDigester {

    /**
     * 对密码进行信息摘要
     *
     * @param password 密码
     * @return {@link String}
     */
    String digest(String password);

    /**
     * 密码与密文进行对比
     *
     * @param password 密码
     * @param digest   摘要，即密码密文
     * @return boolean
     */
    boolean match(String password, String digest);

}
