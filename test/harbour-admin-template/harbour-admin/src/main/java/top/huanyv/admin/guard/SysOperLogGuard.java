package top.huanyv.admin.guard;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;

import top.huanyv.admin.dao.SysOperLogDao;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.admin.service.SysOperLogService;
import top.huanyv.admin.utils.WebUtil;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.DefaultSqlContext;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.webmvc.annotation.Guard;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.MethodRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.guard.NavigationGuard;
import top.huanyv.webmvc.security.SubjectHolder;
import top.huanyv.webmvc.security.User;

/**
 * @author huanyv
 * @date 2023/4/22 14:42
 */
// @Bean
@Guard(value = "/**", exclude = "/admin/logout")
public class SysOperLogGuard implements NavigationGuard {

    @Inject
    private SysOperLogService operLogService;

    @Override
    public void afterEach(HttpRequest req, HttpResponse resp, RequestHandler handler) throws Exception {
        if (handler instanceof MethodRequestHandler) {
            MethodRequestHandler methodHandler = (MethodRequestHandler) handler;
            User subject = SubjectHolder.getStrategy().getSubject();
            handlerOperLog(req.method(), WebUtil.getIP(req), subject.getUsername(), methodHandler.getMethod());
        }
    }


    private void handlerOperLog(String method, String ip, String username, Method controller) {
        new Thread(() -> {
            String ipAddr = WebUtil.getIpAddr(ip);
            SysOperLog sysOperLog = new SysOperLog();
            sysOperLog.setOperType(method.toUpperCase());
            sysOperLog.setOperClass(controller.getDeclaringClass().getName());
            sysOperLog.setOperMethod(controller.getName());
            sysOperLog.setOperUser(username);
            sysOperLog.setOperIp(ip);
            sysOperLog.setOperAddr(ipAddr);
            sysOperLog.setOperDate(new Date());

            operLogService.insert(sysOperLog);
        }).start();
    }


}
