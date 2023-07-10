package top.huanyv.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanyv
 * @date 2023/4/25 19:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogPageDto {

    private String loginUser;

    private String loginAddr;
    /**
     * 登录状态 1-登录成功 0-登录失败
     */
    private String status;

    private String loginDateRange;
}
