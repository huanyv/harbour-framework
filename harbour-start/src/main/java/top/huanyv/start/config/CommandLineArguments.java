package top.huanyv.start.config;

import top.huanyv.tools.utils.StringUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/12/17 14:48
 */
public class CommandLineArguments {

    public static final String PREFIX = "--";

    public static final String ENV_KEY = "app.env";

    private final Map<String, String> argumentMap = new ConcurrentHashMap<>();

    public CommandLineArguments(String... args) {
        for (String arg : args) {
            String[] argMap = arg.substring(PREFIX.length()).split("=");
            if (argMap.length == 1) {
                this.argumentMap.put(argMap[0], "");
            } else {
                this.argumentMap.put(argMap[0], argMap[1]);
            }
        }
    }

    public String getEnv() {
        String env = this.argumentMap.get(ENV_KEY);
        if (!StringUtil.hasText(env)) {
            return Constants.DEFAULT_CONFIG_FILE_NAME;
        }
        return "application-" + env + ".properties";
    }

    public Set<String> keySet() {
        return this.argumentMap.keySet();
    }

    public String get(String key) {
        return this.argumentMap.get(key);
    }

    @Override
    public String toString() {
        return this.argumentMap.toString();
    }
}
