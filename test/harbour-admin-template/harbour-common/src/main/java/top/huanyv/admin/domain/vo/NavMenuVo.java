package top.huanyv.admin.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/2/22 14:08
 */
@Data
public class NavMenuVo {
    private final Map<String,String> homeInfo = new HashMap<>();
    private final Map<String,String> logoInfo = new HashMap<>();
    private Object menuInfo;

    public NavMenuVo() {
        this.homeInfo.put("title", "首页");
        this.homeInfo.put("href", "/welcome");
        this.logoInfo.put("title", "后台管理系统");
        this.logoInfo.put("image", "/static/layui/images/logo.png");
        this.logoInfo.put("href", "");
    }

    public NavMenuVo(Object menuInfo) {
        this();
        this.menuInfo = menuInfo;
    }
}
