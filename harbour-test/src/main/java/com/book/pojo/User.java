package com.book.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.huanyv.jdbc.annotation.TableName;
import top.huanyv.tools.annotation.JavaBean;

/**
 * @author huanyv
 * @date 2022/11/17 17:23
 */
@TableName("t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
}
