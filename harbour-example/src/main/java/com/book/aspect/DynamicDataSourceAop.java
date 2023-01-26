package com.book.aspect;


import top.huanyv.jdbc.support.AbstractDataSourceAop;

/**
 * @author huanyv
 * @date 2023/1/9 14:57
 */
public class DynamicDataSourceAop extends AbstractDataSourceAop {
    @Override
    public String getDataSourceKey() {
        return "ds2";
    }
}
