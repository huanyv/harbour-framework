package top.huanyv.start.config;

import top.huanyv.start.exception.ConfigFileNotFountException;
import top.huanyv.tools.utils.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/12/17 14:41
 */
public class AppArguments {

    private final Map<String, String> argumentMap = new ConcurrentHashMap<>();

    public AppArguments(CommandLineArguments commandLineArguments) {
        // 获取配置文件名
        String envName = commandLineArguments.getEnv();
        InputStream inputStream = ClassLoaderUtil.getInputStream(envName);

        if (!StartConstants.DEFAULT_CONFIG_FILE_NAME.equals(envName) && inputStream == null) {
            throw new ConfigFileNotFountException("The configuration file named '" + envName + "' does not exist!");
        }

        // 加载配置文件
        Properties properties = PropertiesUtil.load(inputStream);
        for (String propertyName : properties.stringPropertyNames()) {
            this.argumentMap.put(propertyName, properties.getProperty(propertyName));
        }

        // 命令行配置覆盖
        for (String key : commandLineArguments.keySet()) {
            this.argumentMap.put(key, commandLineArguments.get(key));
        }
    }

    public AppArguments add(String name, String value) {
        this.argumentMap.put(name, value);
        return this;
    }

    public String get(String name) {
        return argumentMap.get(name);
    }

    public String get(String name, String defaultValue) {
        String value = this.argumentMap.get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public int getInt(String name) {
        return Integer.parseInt(get(name));
    }

    public int getInt(String name, String defaultValue) {
        return Integer.parseInt(get(name, defaultValue));
    }

    public long getLong(String name) {
        return Long.parseLong(get(name));
    }

    public long getLong(String name, String defaultValue) {
        return Long.parseLong(get(name, defaultValue));
    }

    /**
     * 以指定前缀，把参数属性填充到一个对象中
     *
     * @param prefix 前缀
     * @param o      o
     */
    public void populate(String prefix, Object o) {
        Class<?> cls = o.getClass();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            String stringValue = this.get(prefix + field.getName());
            Object val = null;
            if (StringUtil.hasText(stringValue)) {
                val = BeanUtil.numberConvert(field.getType(), stringValue);
                try {
                    if (val != null) {
                        field.set(o, val);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return argumentMap.toString();
    }
}
