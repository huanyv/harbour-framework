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


    /**
     * 重定向视图
     *
     * @param name 视图名
     * @return {@link String}
     */
    public static String redirectView(String name) {
        return "redirect:" + name;
    }

    /**
     * 转发视图
     *
     * @param name 视图名
     * @return {@link String}
     */
    public static String forwardView(String name) {
        return "forward:" + name;
    }

}
