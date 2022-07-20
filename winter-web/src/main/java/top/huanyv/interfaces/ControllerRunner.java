package top.huanyv.interfaces;

import top.huanyv.core.Winter;
import top.huanyv.utils.UrlConver;

/**
 * @author admin
 * @date 2022/7/6 16:47
 */
public interface ControllerRunner {
    void run(Winter app, UrlConver urlConver);
}
