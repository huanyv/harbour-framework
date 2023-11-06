package top.huanyv.bean.utils;

public class FileUtil {


    /**
     * 获取文件后缀名
     *
     * @param fileName 字符串
     * @return 后缀名
     */
    public static String getExtension(String fileName) {
        if (!hasExtension(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 判断有没有后缀
     *
     * @param fileName 字符串
     * @return true/false
     */
    public static boolean hasExtension(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            return false;
        }
        String endStr = fileName.substring(fileName.lastIndexOf("."));
        if (endStr.matches("^\\.[0-9a-zA-Z]+$")) {
            return true;
        }
        return false;
    }


    /**
     * 解析大小为字节数，支持的单位有KB MB GB TB
     *
     * @param sizeStr 文件大小字符串
     * @return long
     */
    public static long parseSize(String sizeStr) {
        Assert.isTrue(StringUtil.hasText(sizeStr), "'sizeStr' must not be empty.");
        try {
            return Long.parseLong(sizeStr);
        } catch (NumberFormatException ignored) {
        }
        int index = sizeStr.length() - 2;
        long size = 0;
        try {
            size = Long.parseLong(sizeStr.substring(0, index));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("size '" + sizeStr + "' unable to parse.");
        }
        String unit = sizeStr.substring(index);
        if ("kb".equalsIgnoreCase(unit)) {
            return size * 1024;
        } else if ("mb".equalsIgnoreCase(unit)) {
            return size * 1024 * 1024;
        } else if ("gb".equalsIgnoreCase(unit)) {
            return size * 1024 * 1024 * 1024;
        } else if ("tb".equalsIgnoreCase(unit)) {
            return size * 1024 * 1024 * 1024 * 1024;
        }
        throw new IllegalArgumentException("size '" + sizeStr + "' unable to parse, '" + unit + "'unit does not support.");
    }

}
