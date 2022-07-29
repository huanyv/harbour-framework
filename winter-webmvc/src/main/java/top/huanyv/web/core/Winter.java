package top.huanyv.web.core;

import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;

/**
 * @author admin
 * @date 2022/7/29 15:09
 */
public interface Winter {

    Winter get(String urlPattern, ServletHandler handler);

    Winter post(String urlPattern, ServletHandler handler);

    Winter put(String urlPattern, ServletHandler handler);

    Winter delete(String urlPattern, ServletHandler handler);

    Winter route(String urlPattern, ServletHandler handler);

    void register(String urlPattern, ServletHandler handler, RequestMethod method);

    default void start() {

    }

}
