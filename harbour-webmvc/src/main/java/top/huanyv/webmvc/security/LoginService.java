package top.huanyv.webmvc.security;

/**
 * @author huanyv
 * @date 2023/3/22 21:09
 */
public interface LoginService {

    User loadUserByUsername(String username);

}
