package top.huanyv.boot.config;

/**
 * @author admin
 * @date 2022/7/6 9:05
 */
public class BootGlobalConfig {

    public static final String DEFAULT_SERVER_PORT = "8090";
    public static final String DEFAULT_SERVLET_CONTEXT = "";
    public static final String DEFAULT_GLOBAL_ENCODING = "UTF-8";

    public static final String CONFIG_KEY_SERVER_PORT = "server.port";
    public static final String CONFIG_KEY_SERVER_CONTEXT = "server.context-path";
    public static final String CONFIG_KEY_GLOBAL_ENCODING = "server.encoding";

    public static final String DEFAULT_CONFIG_PROPERTIES = "application.properties";
    public static final String ENV_COMMAND_LINE_ARGUMENT_NAME = "--app.env";
    public static final String PORT_COMMAND_LINE_ARGUMENT_NAME = "--server.port";

    public static final String BANNER_FILE_NAME = "banner.txt";
    public static final String DEFAULT_BANNER = "__        ___       _            \n" +
            "\\ \\      / (_)_ __ | |_ ___ _ __ \n" +
            " \\ \\ /\\ / /| | '_ \\| __/ _ \\ '__|\n" +
            "  \\ V  V / | | | | | ||  __/ |   \n" +
            "   \\_/\\_/  |_|_| |_|\\__\\___|_|   ";
}
