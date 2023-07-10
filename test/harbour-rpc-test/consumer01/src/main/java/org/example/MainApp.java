package org.example;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.core.HarbourApplication;

import java.util.Arrays;

/**
 * @author huanyv
 * @date 2023/1/22 16:26
 */
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = HarbourApplication.run(MainApp.class, args);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println("beanDefinitionNames = " + Arrays.toString(beanDefinitionNames));
    }
}
