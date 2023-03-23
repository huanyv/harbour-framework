package top.huanyv.webmvc.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * 用户认证后的主体类
 *
 * @author huanyv
 * @date 2023/3/22 20:58
 */
public class LoginUser implements User {

    private String username;

    private String password;

    private boolean status;

    private String[] permissions;

    public LoginUser(String username, String password, boolean status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public LoginUser(String username, String password, boolean status, String... permissions) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.permissions = permissions;
    }

    public LoginUser(String username, String password, boolean status, Collection<String> permissions) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.permissions = permissions.toArray(new String[0]);
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public String[] getPermissions() {
        return permissions;
    }

}
