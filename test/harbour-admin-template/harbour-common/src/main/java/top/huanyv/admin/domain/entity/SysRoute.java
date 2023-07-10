package top.huanyv.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanyv
 * @date 2023/5/7 20:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoute {
    private String urlPattern;
    private String routeMethod; // GET/POST
    private String routeType;// lambda/controller
    private String[] routeArgs;
    private String routeResp;
}
