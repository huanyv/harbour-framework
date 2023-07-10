package top.huanyv.admin.utils;

import lombok.Data;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/22 14:13
 */
@Data
public class MenuNode {
    private Long id;
    private Long parentId;

    private String title;
    private String icon;
    private String href;
    private String target;

    private List<MenuNode> child;
}
