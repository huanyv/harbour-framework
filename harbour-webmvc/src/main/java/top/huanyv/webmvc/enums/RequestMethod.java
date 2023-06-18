package top.huanyv.webmvc.enums;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return method;
    }

}
