package top.huanyv.webmvc.resource;

import java.io.InputStream;

/**
 * @author huanyv
 * @date 2022/10/15 18:35
 */
public interface ResourceHolder {

    InputStream getInputStream(String location);

    boolean isMatch(String location);
}
