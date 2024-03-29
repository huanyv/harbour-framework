package top.huanyv.webmvc.security;

import java.util.Collection;

/**
 * 用户认证后的主体类
 *
 * @author huanyv
 * @date 2023/3/22 20:58
 */
public class LoginUser implements User {

    private String id;

    private String username;

    private String password;

    private boolean status;

    private String[] permissions;

    public LoginUser(String id, String username, String password, boolean status) {
        this(id, username, password, status, new String[0]);
    }

    public LoginUser(String id, String username, String password, boolean status, String... permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.permissions = permissions;
    }

    public LoginUser(String id, String username, String password, boolean status, Collection<String> permissions) {
        this(id, username, password, status, permissions.toArray(new String[0]));
    }

    @Override
    public String getId() {
        return this.id;
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
