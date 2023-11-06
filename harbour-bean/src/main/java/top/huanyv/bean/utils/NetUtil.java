package top.huanyv.bean.utils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huanyv
 * @date 2023/2/12 14:42
 */
public final class NetUtil {
    private NetUtil() {
    }

    /**
     * 端口是否合法
     *
     * @param port 端口号
     * @return boolean
     */
    public static boolean isLegalPort(int port) {
        return port >= 0 && port <= 65535;
    }

    /**
     * 是否可用端口，返回false为端口被占用
     *
     * @param port 端口号
     * @return boolean
     */
    public static boolean isAvailablePort(int port) {
        return !isUsedPort(port);
    }

    /**
     * 端口是否在使用中
     *
     * @param port 端口号
     * @return boolean true 使用中/false 空闲中
     */
    public static boolean isUsedPort(int port) {
        if (!isLegalPort(port)) {
            return false;
        }
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
        try (Socket socket = new Socket()) {
            socket.connect(socketAddress, 1);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static List<String> getLocalIPv4() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    String ip = interfaceAddress.getAddress().getHostAddress();
                    if (isIPv4(ip)) {
                        ips.add(ip);
                    }
                }
            }
            return ips;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(Collections.singletonList("127.0.0.1"));
    }

    public static boolean isIPv4(String ip) {
        Pattern pattern = Pattern.compile("^(?:(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static boolean isIPv6(String ip) {
        Pattern pattern = Pattern.compile("^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

}
