package top.huanyv.webmvc.security;

import java.io.Serializable;

/**
 * 用户主体
 *
 * @author huanyv
 * @date 2023/3/22 21:02
 */
public interface User extends Serializable {

    String getId();
    /**
     * 获得用户名
     *
     * @return {@link String}
     */
    String getUsername();

    /**
     * 得到密码
     *
     * @return {@link String}
     */
    String getPassword();

    /**
     * 是否可用
     *
     * @return boolean
     */
    boolean isStatus();

    /**
     * 获得权限
     *
     * @return {@link String[]}
     */
    String[] getPermissions();

}
