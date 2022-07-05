package top.huanyv.utils;

import sun.swing.plaf.synth.DefaultSynthStyle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceUtil {

    /**
     * 从类路径加载资源响应到客户端
     * @param name 资源名
     * @param resp 响应体
     * @param contentType 响应Content Type
     */
    public static void responseResource(String name, HttpServletResponse resp, String contentType) throws IOException {
        name = StringUtil.removePrefix(name,"/");
        resp.setContentType(contentType);
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(name);
        ServletOutputStream outputStream = resp.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 检查资源是否存在
     * @param name 资源名
     */
    public static boolean exists(String name) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(name);
        return inputStream != null;
    }

    /**
     * 从类路径下加载<code>properties</code>文件，通过<code>key</code>获取值
     * @param fileName 文件名
     * @param key 键
     * @return 值，没有找到返回NULL
     */
    public static String getProperty(String fileName, String key) {
        return getProperty(fileName, key, null);
    }

    /**
     * 从类路径下加载<code>properties</code>文件，通过<code>key</code>获取值
     * @param fileName 文件名
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getProperty(String fileName, String key, String defaultValue) {
        InputStream stream = ClassLoader.getSystemResourceAsStream(fileName);
        if (stream != null) {
            Properties properties = new Properties();
            try {
                properties.load(stream);
                return properties.getProperty(key, defaultValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }


    public static Properties getProperties(String fileName) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
