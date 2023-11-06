package top.huanyv.admin.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import top.huanyv.bean.utils.IoUtil;
import top.huanyv.webmvc.utils.JsonUtil;
import top.huanyv.webmvc.core.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/4/22 14:11
 */
public final class WebUtil {

    public static String getIP(HttpRequest request) {
        return getIP(request.raw());
    }

    public static String getIP(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }
        return remoteAddr;
    }

    public static String getIpAddr(String ip) {
        String addr = "";
        try {
            URL url = new URL("https://api.iftft.com/API/IP/index.php?ip=" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String res = IoUtil.readStr(is);
                Map o = JsonUtil.fromJson(res, Map.class);
                addr = String.valueOf(((Map) o.get("data")).get("local"));
                is.close();
            }
            conn.disconnect();
        } catch (IOException ignored) {
        }
        return addr;
    }
    public static String getBrowserName(String userAgent) {
        if (userAgent.contains("MSIE")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            return "Google Chrome";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Apple Safari";
        } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            return "Opera";
        } else if (userAgent.contains("Edg")) {
            return "Microsoft Edge";
        } else {
            return "Unknown";
        }
    }
}
