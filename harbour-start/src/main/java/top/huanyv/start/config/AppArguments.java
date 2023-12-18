package top.huanyv.start.config;

import top.huanyv.bean.ioc.Configuration;
import top.huanyv.start.exception.ConfigFileNotFountException;
import top.huanyv.bean.utils.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/12/17 14:41
 */
public class AppArguments implements Configuration {

    private final Map<String, String> argumentMap = new ConcurrentHashMap<>();

    public AppArguments(CliArguments cliArguments) {
        // 获取配置文件名
        String envName = cliArguments.getEnv();
        InputStream inputStream = ClassLoaderUtil.getInputStream(envName);

        // 如果不是默认的配置文件，并且不存在，报异常
        if (!StartConstants.DEFAULT_CONFIG_FILE_NAME.equals(envName) && inputStream == null) {
            throw new ConfigFileNotFountException("The configuration file named '" + envName + "' does not exist!");
        }

        // 加载配置文件
        Properties properties = PropertiesUtil.load(inputStream);
        for (String propertyName : properties.stringPropertyNames()) {
            this.argumentMap.put(propertyName, properties.getProperty(propertyName));
        }

        // 命令行配置覆盖
        for (String key : cliArguments.getNames()) {
            this.argumentMap.put(key, cliArguments.get(key));
        }
    }

    public int getInt(String name) {
        return Integer.parseInt(get(name));
    }

    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(get(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String name) {
        return Long.parseLong(get(name));
    }

    public long getLong(String name, long defaultValue) {
        try {
            return Long.parseLong(get(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return this.argumentMap;
    }

    @Override
    public String toString() {
        return argumentMap.toString();
    }
}
