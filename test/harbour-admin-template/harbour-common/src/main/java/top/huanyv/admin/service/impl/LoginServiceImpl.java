package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.UserDao;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.webmvc.security.LoginService;
import top.huanyv.webmvc.security.LoginUser;
import top.huanyv.webmvc.security.User;
import top.huanyv.webmvc.utils.ServletHolder;

import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/25 17:21
 */
@Component
public class LoginServiceImpl implements LoginService {

    @Inject
    private UserDao userDao;

    @Override
    public User loadUserByUsername(String username) {
        SysUser sysUser = userDao.getUserByUsername(username);
        if (sysUser == null) {
            return null;
        }
        String loginIp = ServletHolder.getRequest().raw().getRemoteAddr();
        sysUser.setLoginIp(loginIp);
        sysUser.setLoginDate(new Date());
        userDao.updateUser(sysUser);
        List<String> permissions = userDao.getPermissionsByUserId(sysUser.getUserId());
        return new LoginUser(sysUser.getUserId().toString(), sysUser.getUserName()
                , sysUser.getPassword(), sysUser.getStatus().equals("0"), permissions);
    }
}
