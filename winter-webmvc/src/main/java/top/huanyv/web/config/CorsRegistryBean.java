package top.huanyv.web.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/4 16:22
 */
public class CorsRegistryBean {
    private String urlPattern;
    private Long maxAge;
    private boolean allowCredentials;
    private String allowedOriginPatterns;
    private String allowedHeaders;
    private List<String> allowedMethods = new ArrayList<>();

    public CorsRegistryBean(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public CorsRegistryBean defaultRule() {
        this.allowedOriginPatterns("*");
        this.allowCredentials(true);
        this.allowedMethods("GET", "POST", "DELETE", "PUT");
        this.allowedHeaders("*");
        this.maxAge(3600L);
        return this;
    }

    public CorsRegistryBean maxAge(Long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CorsRegistryBean allowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
        return this;
    }

    public CorsRegistryBean allowedOriginPatterns(String allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
        return this;
    }

    public CorsRegistryBean allowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
        return this;
    }

    public CorsRegistryBean allowedMethods(String... methods) {
        this.allowedMethods.addAll(Arrays.asList(methods));
        return this;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public boolean getAllowCredentials() {
        return allowCredentials;
    }

    public String getAllowedOriginPatterns() {
        return allowedOriginPatterns;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }
}
