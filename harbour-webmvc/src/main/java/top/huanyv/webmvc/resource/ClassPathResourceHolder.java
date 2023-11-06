package top.huanyv.webmvc.resource;

import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.ClassLoaderUtil;
import top.huanyv.bean.utils.StringUtil;

import java.io.InputStream;

/**
 * @author huanyv
 * @date 2022/10/15 18:37
 */
public class ClassPathResourceHolder implements ResourceHolder{

    public static final String CLASS_PATH_PREFIX = "classpath:";

    @Override
    public InputStream getInputStream(String location) {
        Assert.notNull(location, "location is null.");
        Assert.isTrue(location.startsWith(CLASS_PATH_PREFIX), location + ": prefix not with classpath:");
        String path = StringUtil.removePrefix(location, CLASS_PATH_PREFIX);
        return ClassLoaderUtil.getInputStream(path);
    }

    @Override
    public boolean isMatch(String location) {
        return location != null && location.startsWith(CLASS_PATH_PREFIX);
    }
}
