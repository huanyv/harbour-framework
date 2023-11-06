package top.huanyv.bean.utils;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author admin
 * @date 2022/7/9 14:38
 */
public class IoUtil {

    public static final int DEFAULT_CACHE = 8192;

    /**
     * 从指定流中读取数据，写入到指定流中
     *
     * @param input  输入
     * @param output 输出
     * @throws IOException ioexception
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_CACHE];
        int len = 0;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        output.flush();
    }

    /**
     * 从流中读取字符串
     * @param inputStream 输入流
     * @return 字符串
     */
    public static String readStr(InputStream inputStream) {
        return readStr(inputStream, StandardCharsets.UTF_8);
    }
    public static String readStr(InputStream inputStream, Charset charset) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IoUtil.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return new String(outputStream.toByteArray(), charset);
    }

    /**
     * 写str
     *
     * @param fileName 文件名称
     * @param str      str
     * @throws IOException ioexception
     */
    public static void writeStr(String fileName, String str) throws IOException {
        writeStr(fileName, str, false);
    }

    /**
     * 写str
     *
     * @param fileName 文件名称
     * @param str      str
     * @param append   附加
     * @throws IOException ioexception
     */
    public static void writeStr(String fileName, String str, boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
        writer.write(str);
    }

    /**
     * 关闭流
     *
     * @param closeables closeables
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
