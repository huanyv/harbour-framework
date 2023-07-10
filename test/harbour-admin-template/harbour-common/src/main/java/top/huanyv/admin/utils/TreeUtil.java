package top.huanyv.admin.utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/2/22 14:31
 */
public final class TreeUtil {
    public static List<MenuNode> toTree(List<MenuNode> treeList, Long root) {
        List<MenuNode> children = treeList.stream()
                .filter(menuNode -> root.equals(menuNode.getParentId())) // 找孩子
                .map(menuNode -> {
                    menuNode.setChild(toTree(treeList, menuNode.getId()));
                    return menuNode;
                }) // 给孩子找孩子
                .collect(Collectors.toList());
        return children;
    }
}
