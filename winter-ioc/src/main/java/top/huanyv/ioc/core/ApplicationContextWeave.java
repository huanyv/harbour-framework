package top.huanyv.ioc.core;

/**
 * IOC容器织入接口，通过SPI机制可以自动识别，
 * 在META-INF/services创建top.huanyv.ioc.core.ApplicationContextWeave文件，文件内容为该接口实现类的全类名<br>
 * (createBeanInstance) -> [createBeanInstanceAfter]<br>
 * (proxyBean) -> [populateBeanBefore]<br>
 * (populateBean) -> [populateBeanAfter]
 *
 * @author huanyv
 * @date 2022/9/13 8:12
 */
public interface ApplicationContextWeave {

    default int getOrder() {
        return 0;
    }

    default void createBeanInstanceAfter(ApplicationContext applicationContext) {

    }

    default void populateBeanBefore(ApplicationContext applicationContext) {

    }

    default void populateBeanAfter(ApplicationContext applicationContext){

    }
}
