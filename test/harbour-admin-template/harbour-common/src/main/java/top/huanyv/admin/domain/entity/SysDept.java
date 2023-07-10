package top.huanyv.admin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;

import java.util.Date;
import java.io.Serializable;

/**
 * (SysDept)实体类
 *
 * @author makejava
 * @since 2023-03-27 13:23:37
 */
@Data
@TableName("sys_dept")
public class SysDept {

    @TableId
    private Long deptId;
    
    private String deptName;
    
    private String address;

    private String phonenumber;

    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String updateBy;
    
    private Date updateTime;

    /**
     * 是否可用 ‘0’ 可用  '1' 不可用 
     */
    private String status;
    
    private String remark;

}

