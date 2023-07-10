package top.huanyv.admin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysNotice)实体类
 *
 * @author makejava
 * @since 2023-03-27 13:23:53
 */
@Data
@TableName("sys_notice")
public class SysNotice {

    @TableId
    private Long noticeId;
    
    private String title;
    
    private String content;

    private String isTop;
    
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    private String updateBy;
    
    private Date updateTime;
    
    private String status;

}

