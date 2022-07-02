package top.huanyv.enums;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return method;
    }




}
