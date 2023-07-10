package top.huanyv.admin.controller;

import lombok.extern.slf4j.Slf4j;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.domain.entity.SysMenu;
import top.huanyv.admin.domain.vo.NavMenuVo;
import top.huanyv.admin.service.SysLoginLogService;
import top.huanyv.admin.service.SysMenuService;
import top.huanyv.admin.utils.*;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Post;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.action.Json;
import top.huanyv.webmvc.security.*;
import top.huanyv.webmvc.security.digest.PasswordDigester;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/2/22 14:27
 */
@Component
@Route("/admin")
@Slf4j
public class LoginController {

    @Inject
    private SysMenuService menuService;

    @Inject
    private LoginService loginService;

    @Inject
    private PasswordDigester passwordDigester;

    @Inject
    private SysLoginLogService loginLogService;

    @Get("/loadNavMenus")
    public ActionResult loadNavMenus(HttpSession session) {
        NavMenuVo menuVo = new NavMenuVo();
        User user = SubjectHolder.getStrategy().getSubject();
        List<SysMenu> menus = menuService.getMenusByUserId(Long.valueOf(user.getId()));
        List<MenuNode> menuNodes = menus.stream()
                .map(sysMenu -> {
                    MenuNode menuNode = new MenuNode();
                    menuNode.setId(sysMenu.getMenuId());
                    menuNode.setParentId(sysMenu.getParentId());
                    menuNode.setTitle(sysMenu.getMenuName());
                    menuNode.setHref(sysMenu.getComponent() == null ? "" : sysMenu.getComponent());
                    menuNode.setIcon(sysMenu.getIcon());
                    menuNode.setTarget("");
                    return menuNode;
                }).collect(Collectors.toList());
        List<MenuNode> result = TreeUtil.toTree(menuNodes, 0L);
        menuVo.setMenuInfo(result);
        return new Json(menuVo);
    }

    @Post("/login")
    public ActionResult login(@Param("username") String username, @Param("password") String password, @Param("captcha") String captcha, HttpRequest req) {
        HttpSession session = req.getSession();
        String sessionCaptcha = (String) session.getAttribute(Constants.CAPTCHA_SESSION_KEY);
        if (!StringUtil.equalsIgnoreCase(sessionCaptcha, captcha)) {
            log.info("验证码不正确");
            // return RestResult.error("验证码不正确");
        }
        User user = loginService.loadUserByUsername(username);
        if (user == null) {
            return RestResult.error("用户不存在");
        }
        // 认证用户
        Authenticator authenticator = new Authenticator(user, passwordDigester);
        boolean authenticate = authenticator.authenticate(username, password);
        if (authenticate) {
            // 登录成功，登录用户保存
            SubjectHolder.getStrategy().setSubject(user);
            // 登录日志
            loginLog(user, WebUtil.getIP(req), req.getHeader("User-Agent"), 1);
            return RestResult.ok("登录成功");
        }
        // 登录失败
        loginLog(user, WebUtil.getIP(req), req.getHeader("User-Agent"), 0);
        return RestResult.error("密码不正确");
    }

    @Get("/logout")
    public String logout() {
        SubjectHolder.getStrategy().clear();
        return "redirect:/login";
    }


    public void loginLog(User user, String ip, String userAgent, int status) {
        new Thread(() -> {
            SysLoginLog sysLoginLog = new SysLoginLog();
            sysLoginLog.setLoginUser(user.getUsername());
            sysLoginLog.setLoginIp(ip);
            String ipAddr = WebUtil.getIpAddr(ip);
            sysLoginLog.setLoginAddr(ipAddr);
            sysLoginLog.setBrowser(WebUtil.getBrowserName(userAgent));
            sysLoginLog.setLoginDate(new Date());
            sysLoginLog.setStatus(String.valueOf(status));
            loginLogService.insert(sysLoginLog);
        }).start();
    }


}
