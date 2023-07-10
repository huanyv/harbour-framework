package top.huanyv.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.jdbc.annotation.TableId;

import java.util.Date;

/**
 * @author huanyv
 * @date 2023/3/17 21:07
 */
@Data
public class SysRolesLoadVo {
    /**
     * 角色ID
     */
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
     * 是否选中
     */
    @JsonProperty("LAY_CHECKED")
    private Boolean LAY_CHECKED;
}
