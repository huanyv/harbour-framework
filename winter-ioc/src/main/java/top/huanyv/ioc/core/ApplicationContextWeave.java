package top.huanyv.ioc.core;

/**
 * IOC容器织入接口 <br>
 * (configBean) -> <br>
 * [findBeanBefore] ->(findBeanDefinitions) -> <br>
 * [createBeanBefore] -> (createBean) -> <br>
 * [injectBeanBefore] -> (injectBean) -> [injectBeanAfter] <br>
 *
 * @author huanyv
 * @date 2022/9/13 8:12
 */
public interface ApplicationContextWeave {

    default int getOrder() {
        return 0;
    }

    default void findBeanBefore(ApplicationContext applicationContext) {

    }

    default void createBeanBefore(ApplicationContext applicationContext) {

    }

    default void injectBeanBefore(ApplicationContext applicationContext) {

    }

    default void injectBeanAfter(ApplicationContext applicationContext){

    }
}
