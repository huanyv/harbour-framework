package top.huanyv.core;

import cn.hutool.core.lang.ClassScanner;
import top.huanyv.annotation.Controller;
import top.huanyv.interfaces.ControllerRunner;
import top.huanyv.utils.UrlConver;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2022/7/6 16:46
 */
public class WinterApplication {

    public static void run(Class<?> clazz, String[] args) {
        Set<Class<?>> classes = scanClassBySuperAnno(clazz.getPackage().getName(), ControllerRunner.class, Controller.class);

        Winter app = Winter.use();

        for (Class<?> aClass : classes) {
            try {
                ControllerRunner runner = (ControllerRunner) aClass.getConstructor().newInstance();
                Method run = aClass.getMethod("run", Winter.class, UrlConver.class);
                run.invoke(runner, app, new UrlConver());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        app.start();
    }


    public static Set<Class<?>> scanClassBySuperAnno(String packageName, Class<?> clazz, Class<Controller> annotation) {
        Set<Class<?>> classes = ClassScanner.scanAllPackageBySuper(packageName, clazz);
        return classes.stream()
                .filter(aClass -> aClass.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

}
