package top.huanyv.webmvc.security;

import org.junit.Test;
import top.huanyv.webmvc.security.digest.MDPasswordDigester;

public class AuthenticatorTest {

    @Test
    public void authenticate() {
        // User user = new LoginUser("admin", "$2a$10$kRxvwr1.frwKtOm7fDvYoOgc/3xy9tMLkHrCXulbMvOilDj1uKXQO", true);
        User user = new LoginUser("1", "admin", "e10adc3949ba59abbe56e057f20f883e", true);
        String username = "admin";
        String password = "123456";

        Authenticator authenticator = new Authenticator(user, new MDPasswordDigester("MD5"));

        boolean authenticate = authenticator.authenticate(username, password);

        System.out.println(authenticate ? "登录成功" : "登录失败");
    }
}