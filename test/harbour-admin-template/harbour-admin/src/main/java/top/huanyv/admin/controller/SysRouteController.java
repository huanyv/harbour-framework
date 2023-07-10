package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysRoute;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.PageUtil;
import top.huanyv.bean.annotation.Component;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.request.MethodRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.core.request.RequestMapping;
import top.huanyv.webmvc.enums.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/5/7 20:24
 */
@Component
@Route("/admin/monitor/route")
public class SysRouteController {

    private List<SysRoute> routes;

    @Get
    public ActionResult page(@Form PageDto pageDto, @Param("url") String url) {
        if (routes == null) {
            routes = new ArrayList<>();
            List<RequestMapping> mappings = RequestHandlerRegistry.single().getMappings();
            for (RequestMapping mapping : mappings) {
                Map<RequestMethod, RequestHandler> handlerMapping = mapping.getHandlerMapping();
                for (Map.Entry<RequestMethod, RequestHandler> entry : handlerMapping.entrySet()) {
                    RequestMethod method = entry.getKey();
                    RequestHandler handler = entry.getValue();
                    boolean isController = handler instanceof MethodRequestHandler;
                    SysRoute sysRoute = new SysRoute();
                    sysRoute.setUrlPattern(mapping.getUrlPattern());
                    sysRoute.setRouteMethod(method.getName());
                    sysRoute.setRouteType(isController ? "控制器" : "函数式");
                    String[] args = new String[]{"无"};
                    if (isController) {
                        Class<?>[] parameterTypes = ((MethodRequestHandler) handler).getMethod().getParameterTypes();
                        args = new String[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            args[i] = parameterTypes[i].getName();
                        }
                    }
                    sysRoute.setRouteArgs(args);
                    sysRoute.setRouteResp(isController ? ((MethodRequestHandler) handler).getMethod().getReturnType().getName() : "无");
                    routes.add(sysRoute);
                }
            }
            routes.sort((o1, o2) -> o1.getUrlPattern().compareTo(o2.getUrlPattern()));
        }
        if (StringUtil.hasText(url)) {
            List<SysRoute> collect = routes.stream().filter(route -> route.getUrlPattern().contains(url)).collect(Collectors.toList());
            List<SysRoute> page = PageUtil.page(collect, pageDto.getPage(), pageDto.getLimit());
            return new LayUIPageVo(collect.size(), page);
        }
        List<SysRoute> page = PageUtil.page(routes, pageDto.getPage(), pageDto.getLimit());
        return new LayUIPageVo(routes.size(), page);
    }


}
