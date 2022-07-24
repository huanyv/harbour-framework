package top.huanyv.utils;

/**
 * @author admin
 * @date 2022/7/6 16:49
 */
public class UrlConver {
    public String baseUrl;

    public void setBase(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String toUrl(String url) {
        return baseUrl + url;
    }

}
