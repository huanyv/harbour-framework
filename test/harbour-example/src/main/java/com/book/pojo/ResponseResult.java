package com.book.pojo;

import lombok.Data;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.action.ActionResult;

@Data
public class ResponseResult implements ActionResult {

    public static final Integer OK = 0;
    public static final Integer SERVER_ERROR = 500;

    private Integer code;
    private String msg;
    private Long count;
    private Object data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg, Long count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public static ResponseResult success(String msg) {
        return new ResponseResult(OK, msg, null, null);
    }

    public static ResponseResult fail(String msg) {
        return new ResponseResult(SERVER_ERROR, msg, null, null);
    }

    public static ResponseResult conditionResult(boolean condition, String trueMsg, String falseMsg) {
        if (condition) {
            return success(trueMsg);
        }
        return fail(falseMsg);
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        resp.json(this);
    }
}
