package top.huanyv.admin.domain.entity;

import lombok.Data;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;

import java.util.Date;

/**
 * 菜单权限表(SysMenu)实体类
 *
 * @author makejava
 * @since 2023-02-22 14:56:03
 */
@Data
@TableName("sys_menu")
public class SysMenu {
    /**
     * 菜单ID
     */
    @TableId
    private Long menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;
    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 菜单图标
     */
    private String icon;
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

