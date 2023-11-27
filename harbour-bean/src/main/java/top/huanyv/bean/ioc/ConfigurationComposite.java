package top.huanyv.bean.ioc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2023/11/7 10:02
 */
public class ConfigurationComposite implements Configuration {

    private final Map<String, String> map = new ConcurrentHashMap<>();

    public void put(Configuration configuration) {
        for (String key : configuration.getNames()) {
            String val = configuration.get(key);
            if (val != null) {
                this.map.put(key, val);
            }
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return this.map;
    }

    @Override
    public String toString() {
        return "Configuration=" + map ;
    }

}
