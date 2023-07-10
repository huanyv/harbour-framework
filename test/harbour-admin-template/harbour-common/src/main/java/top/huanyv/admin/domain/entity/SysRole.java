package top.huanyv.admin.domain.entity;

import lombok.Data;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;

import java.lang.annotation.Repeatable;
import java.util.Date;
import java.io.Serializable;

/**
 * 角色信息表(SysRole)实体类
 *
 * @author makejava
 * @since 2023-03-15 20:20:58
 */
@Data
@TableName("sys_role")
public class SysRole {
    /**
     * 角色ID
     */
    @TableId
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色权限字符串
     */
    private String roleKey;
    /**
     * 显示顺序
     */
    private Integer roleSort;
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;
}

