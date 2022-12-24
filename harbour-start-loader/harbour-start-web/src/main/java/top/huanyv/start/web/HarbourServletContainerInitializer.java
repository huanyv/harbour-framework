package top.huanyv.start.web;

import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.tools.utils.ReflectUtil;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author huanyv
 * @date 2022/12/24 16:47
 */
@HandlesTypes(WebStartupInitializer.class)
public class HarbourServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        List<WebStartupInitializer> initializers = new ArrayList<>(c.size());


        for (Class<?> cls : c) {
            if (WebStartupInitializer.class.isAssignableFrom(cls)
                    && !Modifier.isAbstract(cls.getModifiers())
                    && !cls.isInterface()) {
                initializers.add((WebStartupInitializer) ReflectUtil.newInstance(cls));
            }
        }

        for (WebStartupInitializer initializer : initializers) {
            initializer.onStartup(ctx);
        }
    }
}
