package top.huanyv.admin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;

import java.util.Date;
import java.io.Serializable;

/**
 * (SysLoginLog)实体类
 *
 * @author makejava
 * @since 2023-04-22 13:44:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_login_log")
public class SysLoginLog {

    @TableId
    private Long logId;
    
    private String loginUser;
    
    private String loginIp;
    
    private String loginAddr;
    
    private String browser;
    /**
     * 登录状态 1-登录成功 0-登录失败
     */
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDate;

}

