package top.huanyv.admin.controller;

import top.huanyv.admin.domain.server.Server;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.domain.vo.ioc.BeanVo;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.PageUtil;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.aop.AopContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.request.Model;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/4/21 20:49
 */
@Component
@Route("/admin/monitor/beans")
public class SysBeanController {

    private List<BeanVo> beans;

    @Get
    @Body
    public ActionResult ioc(HttpRequest request, Model model, @Form PageDto pageDto, @Form BeanVo beanDto) {
        if (beans == null) {
            beans = new ArrayList<>();
            ApplicationContext context = (ApplicationContext) request.ctx().getAttribute(WebMvcGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME);
            String[] beanDefinitionNames = context.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = context.getBeanDefinition(beanDefinitionName);
                Class<?> beanClass = beanDefinition.getBeanClass();
                BeanVo beanVo = new BeanVo();
                beanVo.setBeanName(beanDefinitionName);
                beanVo.setBeanClass(beanClass.getName());
                beanVo.setBeanType(beanDefinition.getClass().getSimpleName());
                beanVo.setSingleton(beanDefinition.isSingleton());
                beanVo.setLazy(beanDefinition.isLazy());
                beanVo.setAop(AopContext.isNeedProxy(beanClass));
                Class<?>[] interfaces = beanClass.getInterfaces();
                String[] ins = new String[interfaces.length];
                for (int i = 0; i < interfaces.length; i++) {
                    ins[i] = interfaces[i].getName();
                }
                beanVo.setInterfaces(ins);
                Class<?> superclass = beanClass.getSuperclass();
                beanVo.setSuperclass(superclass == null ? null : superclass.getName());
                Field[] fields = beanClass.getDeclaredFields();
                String[] fs = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fs[i] = fields[i].getName();
                }
                beanVo.setFields(fs);
                Method[] methods = beanClass.getDeclaredMethods();
                String[] ms = new String[methods.length];
                for (int i = 0; i < methods.length; i++) {
                    ms[i] = methods[i].getName();
                }
                beanVo.setMethods(ms);
                beans.add(beanVo);
            }
        }

        List<BeanVo> beanVos = beans.stream().filter(beanVo -> {
            boolean name = true;
            boolean cls = true;
            if (StringUtil.hasText(beanDto.getBeanName())) {
                name = beanVo.getBeanName().contains(beanDto.getBeanName());
            }
            if (StringUtil.hasText(beanDto.getBeanClass())) {
                cls = beanVo.getBeanClass().contains(beanDto.getBeanClass());
            }
            return name && cls;
        }).collect(Collectors.toList());

        List<BeanVo> result = PageUtil.page(beanVos, pageDto.getPage(), pageDto.getLimit());
        return new LayUIPageVo(beans.size(), result);
    }

}
