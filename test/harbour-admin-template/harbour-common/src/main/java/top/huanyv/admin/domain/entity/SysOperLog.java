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
 * 操作日志表(SysOperLog)实体类
 *
 * @author makejava
 * @since 2023-05-07 16:41:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_oper_log")
public class SysOperLog {

    @TableId
    private Long logId;
    /**
     * 操作用户
     */
    private String operUser;
    /**
     * 操作人IP
     */
    private String operIp;
    /**
     * 操作人地址
     */
    private String operAddr;
    /**
     * 操作类、模块
     */
    private String operClass;
    /**
     * 操作方法
     */
    private String operMethod;
    /**
     * 操作类型
     */
    private String operType;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operDate;


}

