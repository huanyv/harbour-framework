package top.huanyv.start.web.initialize;

import top.huanyv.bean.utils.ReflectUtil;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 部署war包在tomcat容器中，执行的类
 *
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
