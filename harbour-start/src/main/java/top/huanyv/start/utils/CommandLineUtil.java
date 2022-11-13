package top.huanyv.start.utils;

import top.huanyv.tools.utils.StringUtil;

/**
 * @author admin
 * @date 2022/7/30 15:42
 */
public class CommandLineUtil {

    private String[] args;


    public CommandLineUtil(String[] args) {
        this.args = args;
    }

    public String getArgumentValue(String name, String defaultValue) {
        if (this.args.length <= 0) {
            return defaultValue;
        }
        for (String arg : this.args) {
            String[] argument = arg.split("=");
            if (argument.length == 2 && argument[0].equals(name) && StringUtil.hasText(argument[1])) {
                return argument[1];
            }
        }
        return defaultValue;
    }

}
