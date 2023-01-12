package com.book.aspect;

import top.huanyv.jdbc.support.AbstractDataSourceAOP;

/**
 * @author huanyv
 * @date 2023/1/9 14:57
 */
public class DynamicDataSourceAOP extends AbstractDataSourceAOP {
    @Override
    public String getDataSourceKey() {
        return "ds2";
    }
}
