package top.huanyv.webmvc.security.digest;

import top.huanyv.tools.utils.BCrypt;

/**
 * @author huanyv
 * @date 2023/3/23 17:23
 */
public class BCryptPasswordDigester implements PasswordDigester {

    @Override
    public String digest(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean match(String password, String digest) {
        return BCrypt.checkpw(password, digest);
    }

}
