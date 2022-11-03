package top.huanyv.core.test.entity;

/**
 * @author huanyv
 * @date 2022/11/3 10:55
 */
public class User {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
