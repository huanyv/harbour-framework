package top.huanyv.webmvc.config;

/**
 * @author admin
 * @date 2022/7/6 8:56
 */
public interface WebConfigurer {

    default void addViewController(ViewControllerRegistry registry) {

    }


    default void addResourceMapping(ResourceMappingRegistry registry) {

    }

    default void configNavigationRegistry(NavigationGuardRegistry registry) {

    }

    default void addCorsMappings(CorsRegistry registry) {

    }


}
