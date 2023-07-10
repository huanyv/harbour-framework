package top.huanyv.admin.utils;

import top.huanyv.tools.enums.HttpStatus;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.action.Json;

import java.util.HashMap;

public class RestResult extends HashMap<String, Object> implements ActionResult {

    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    public RestResult() {
        super();
        this.put(CODE, 0);
        this.put(MSG, "操作成功");
        this.put(DATA, new Object[0]);
    }

    public RestResult(int code) {
        this();
        this.put(CODE, code);
    }

    public RestResult(int code, String msg) {
        this();
        this.put(CODE, code);
        this.put(MSG, msg);
    }

    public RestResult(String msg, Object data) {
        this();
        this.put(MSG, msg);
        this.put(DATA, data);
    }

    public RestResult(int code, String msg, Object data) {
        this();
        this.put(CODE, code);
        this.put(MSG, msg);
        this.put(DATA, data);
    }

    public RestResult(HttpStatus status) {
        this();
        this.put(CODE, status.getCode());
        this.put(MSG, status.getMsg());
    }

    public RestResult(HttpStatus status, Object data) {
        this();
        this.put(CODE, status.getCode());
        this.put(MSG, status.getMsg());
        this.put(DATA, data);
    }

    public RestResult add(String name, String val) {
        this.put(name, val);
        return this;
    }

    public static RestResult status(HttpStatus status) {
        return new RestResult(status);
    }

    public static RestResult ok(Object data) {
        return new RestResult(0, "操作成功", data);
    }

    public static RestResult ok(String msg) {
        return new RestResult(0, msg);
    }

    public static RestResult ok(String msg, Object data) {
        return new RestResult(msg, data);
    }

    public static RestResult error(String msg) {
        return new RestResult(500, msg);
    }

    public static RestResult condition(boolean condition, String okMsg, String errorMsg) {
        if (condition) {
            return ok(okMsg);
        }
        return error(errorMsg);
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        new Json(this).execute(req, resp);
    }
}
