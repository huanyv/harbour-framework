package top.huanyv.bean.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 通过包扫描{@link Class Class<?>}对象
 *
 * @author huanyv
 * @date 2023/11/12
 */
public class ClassScanner {

    // 存放已被扫描过的，避免被多次扫描
    private final Set<Class<?>> scannedClass = new HashSet<>();

    // 扫描的基础 package，只扫描该包及其子包之下的类
    private String basePackage;

    private ClassLoader classLoader;

    public ClassScanner(String basePackage) {
        if (basePackage == null || basePackage.equals("")) {
            throw new IllegalArgumentException("basePackage can not be blank");
        }

        String bp = basePackage.replace('.', '/');
        bp = bp.endsWith("/") ? bp : bp + '/'; // 添加后缀字符 '/'
        bp = bp.startsWith("/") ? bp.substring(1) : bp; // 删除前缀字符 '/'

        this.basePackage = bp;
    }

    public Set<Class<?>> scan() {
        try {
            classLoader = getClassLoader();
            List<URL> urlList = getResources();
            scanResources(urlList);
            return scannedClass;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : ClassScanner.class.getClassLoader();
    }

    private List<URL> getResources() throws IOException {
        List<URL> ret = new ArrayList<>();

        // 用于去除重复
        Set<String> urlSet = new HashSet<>();
        // ClassLoader.getResources(...) 参数只支持包路径分隔符为 '/'，而不支持 '\'
        Enumeration<URL> urls = classLoader.getResources(basePackage);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            String urlStr = url.toString();
            if (!urlSet.contains(urlStr)) {
                urlSet.add(urlStr);
                ret.add(url);
            }
        }
        return ret;
    }

    private void scanResources(List<URL> urlList) throws IOException {
        for (URL url : urlList) {
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                scanJar(url);
            } else if ("file".equals(protocol)) {
                scanFile(url);
            }
        }
    }

    private void scanJar(URL url) throws IOException {
        URLConnection urlConn = url.openConnection();
        if (urlConn instanceof JarURLConnection) {
            JarURLConnection jarUrlConn = (JarURLConnection) urlConn;
            try (JarFile jarFile = jarUrlConn.getJarFile()) {
                Enumeration<JarEntry> jarFileEntries = jarFile.entries();
                while (jarFileEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarFileEntries.nextElement();
                    String en = jarEntry.getName();
                    // 只扫描 basePackage 之下的类
                    if (en.endsWith(".class") && en.startsWith(basePackage)) {
                        // JarEntry.getName() 返回值中的路径分隔符在所有操作系统下都是 '/'
                        en = en.substring(0, en.length() - 6).replace('/', '.');
                        scanClass(en);
                    }
                }
            }
        }
    }

    private void scanFile(URL url) {
        String path = url.getPath();
        path = decodeUrl(path);
        File file = new File(path);
        String classPath = getClassPath(file);
        scanFile(file, classPath);
    }

    private void scanFile(File file, String classPath) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File fi : files) {
                    scanFile(fi, classPath);
                }
            }
        } else if (file.isFile()) {
            String fullName = file.getAbsolutePath();
            if (fullName != null && fullName.endsWith(".class")) {
                String className = fullName.substring(classPath.length(), fullName.length() - 6).replace(File.separatorChar, '.');
                scanClass(className);
            }
        }
    }

    private String getClassPath(File file) {
        String ret = file.getAbsolutePath();
        // 添加后缀，以便后续的 indexOf(bp) 可以正确获得下标值，因为 bp 确定有后缀
        if (!ret.endsWith(File.separator)) {
            ret = ret + File.separator;
        }

        // 将 basePackage 中的路径分隔字符转换成与 OS 相同，方便处理路径
        String bp = basePackage.replace('/', File.separatorChar);
        int index = ret.lastIndexOf(bp);
        if (index != -1) {
            ret = ret.substring(0, index);
        }

        return ret;
    }

    private void scanClass(String className) {
        // 跳过不需要被扫描的 className
        Class<?> c = loadClass(className);
        if (c != null && !scannedClass.contains(c)) {
            // 确保 class 只被扫描一次
            scannedClass.add(c);
        }
    }

    private Class<?> loadClass(String className) {
        try {
            return classLoader.loadClass(className);
        }
        // 此处不能 catch Exception，否则抓不到 NoClassDefFoundError，因为它是 Error 的子类
        catch (Throwable t) {
            //log.debug("PathScanner can not load the class \"" + className + "\"");

            /**
             * 由于扫描是一种主动行为，所以 pom.xml 中的 provided 依赖会在此被 loadClass， 从而抛出
             * NoClassDefFoundError、UnsupportedClassVersionError、 ClassNotFoundException
             * 异常。return null 跳过这些 class 不处理
             *
             * 如果这些异常并不是 provided 依赖的原因而引发，也会在后续实际用到它们时再次抛出异常， 所以 return null 并不会错过这些异常
             */
            return null;
        }
    }

    /**
     * 支持路径中存在空格百分号等等字符
     */
    private String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}