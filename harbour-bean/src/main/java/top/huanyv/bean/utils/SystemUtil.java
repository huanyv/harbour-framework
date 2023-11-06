package top.huanyv.bean.utils;

/**
 * @author huanyv
 * @date 2023/2/8 20:50
 */
public class SystemUtil {

    private static final String SYSTEM_OS_NAME = System.getProperty("os.name");

    public static boolean isWindows() {
        return SYSTEM_OS_NAME.toLowerCase().contains("windows");
    }

    public static boolean isLinux() {
        return SYSTEM_OS_NAME.toLowerCase().contains("linux");
    }

    public static boolean isIntellijIDEA() {
        try {
            Class.forName("com.intellij.rt.execution.application.AppMainV2");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String getOsName() {
        return SYSTEM_OS_NAME;
    }

}
