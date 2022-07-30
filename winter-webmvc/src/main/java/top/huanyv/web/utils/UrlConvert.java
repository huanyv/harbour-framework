package top.huanyv.web.utils;

/**
 * @author admin
 * @date 2022/7/6 16:49
 */
public class UrlConvert {
    public String baseUrl;

    public UrlConvert() {
    }

    public UrlConvert(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBase(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String toUrl(String url) {
        return baseUrl + url;
    }

}
