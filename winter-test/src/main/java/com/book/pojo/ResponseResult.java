package com.book.pojo;

import lombok.Data;

@Data
public class ResponseResult {

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
}
