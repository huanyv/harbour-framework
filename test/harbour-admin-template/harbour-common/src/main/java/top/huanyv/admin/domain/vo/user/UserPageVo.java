package top.huanyv.admin.domain.vo.user;

import lombok.Data;
import top.huanyv.admin.domain.entity.SysDept;

/**
 * @author huanyv
 * @date 2023/2/23 20:27
 */
@Data
public class UserPageVo {
    /**
     * 用户ID
     */
    private Long userId;

    private Long deptId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

    private SysDept dept;
}
