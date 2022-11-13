package top.huanyv.webmvc.utils;

/**
 * @author admin
 * @date 2022/7/6 16:49
 */
public class UrlConvert {
    public String baseUrl;

    public UrlConvert(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String to(String url) {
        return baseUrl + url;
    }

}
