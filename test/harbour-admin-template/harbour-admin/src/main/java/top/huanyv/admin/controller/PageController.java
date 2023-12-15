package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.admin.domain.server.Server;
import top.huanyv.admin.service.SysNoticeService;
import top.huanyv.admin.service.SysRoleService;
import top.huanyv.admin.service.SysUserService;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.action.View;
import top.huanyv.webmvc.core.request.Model;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.security.LoginService;
import top.huanyv.webmvc.security.SubjectHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/3/27 21:09
 */
@Bean
@Route
public class PageController {

    @Inject
    private SysNoticeService sysNoticeService;

    @Inject
    private SysUserService sysUserService;

    @Inject
    private SysRoleService sysRoleService;

    @Route("/welcome")
    public ActionResult welcome(Model model) {
        List<SysNotice> notices = sysNoticeService.getNewNotice();
        model.add("notices", notices);
        return new View("welcome/welcome");
    }

    @Route("/user/info")
    public ActionResult userInfo(Model model) {
        String loginUserId = LoginUtil.getLoginUserId();
        SysUser user = sysUserService.getUserById(Long.valueOf(loginUserId));
        model.add("user", user);

        List<SysRole> sysRoles = sysRoleService.listRoleByUserId(user.getUserId());
        List<String> roleNames = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        String roles = String.join("/", roleNames);
        model.add("roles", roles);

        return new View("system/sysuser/info");
    }

    @Route("/monitor/server")
    public ActionResult server(Model model) throws Exception {
        Server server = new Server();
        server.copyTo();
        model.add("server", server);
        return new View("monitor/server");
    }
}
