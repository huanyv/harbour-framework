package top.huanyv.webmvc.view;

import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author huanyv
 * @date 2022/10/15 19:05
 */
public class FileSystemResourceHolder implements ResourceHolder{

    public static final String FILE_SYSTEM_PREFIX = "file:";

    @Override
    public InputStream getInputStream(String location) {
        Assert.notNull(location, "location is null.");
        Assert.isTrue(location.startsWith(FILE_SYSTEM_PREFIX), location + ": prefix not with file:");
        try {
            return new FileInputStream(StringUtil.removePrefix(location, FILE_SYSTEM_PREFIX));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isMatch(String location) {
        return location != null && location.startsWith(FILE_SYSTEM_PREFIX);
    }
}
