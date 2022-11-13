package top.huanyv.webmvc.config;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.guard.NavigationGuard;

import java.util.List;

/**
 * @author admin
 * @date 2022/8/4 16:35
 */
public class CorsGuard implements NavigationGuard {

    private Long maxAge;
    private boolean allowCredentials;
    private String allowedOriginPatterns;
    private String allowedHeaders;
    private List<String> allowedMethods;


    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", this.allowedOriginPatterns);
        resp.setHeader("Access-Control-Allow-Methods", String.join(", ", this.allowedMethods));
        resp.setHeader("Access-Control-Max-Age", this.maxAge.toString());
        resp.setHeader("Access-Control-Allow-Headers", this.allowedHeaders);
        resp.setHeader("Access-Control-Allow-Credentials", String.valueOf(this.allowCredentials));
        return true;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public void setAllowedOriginPatterns(String allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }
}
