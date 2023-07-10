package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysMenu;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.service.SysMenuService;
import top.huanyv.admin.service.SysRoleService;
import top.huanyv.admin.utils.Constants;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.action.Json;
import top.huanyv.webmvc.security.HasPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.huanyv.admin.utils.Constants.*;

/**
 * @author huanyv
 * @date 2023/2/22 14:23
 */
@Component
@Route("/admin/menu")
public class SysMenuController {

    @Inject
    private SysMenuService menuService;

    @Inject
    private SysRoleService roleService;

    @Get
    @HasPermission("system:menu:list")
    public ActionResult list() {
        List<SysMenu> menus = menuService.getMenus();
        return new LayUIPageVo(menus.size(), menus);
    }

    @Post("/listMenuTree")
    public ActionResult listMenuTree() {
        List<SysMenu> menus = menuService.getMenus();
        List<Map<String, Object>> menuNodes = new ArrayList<>();
        for (SysMenu menu : menus) {
            Map<String, Object> menuNode = new HashMap<>();
            menuNode.put("id", menu.getMenuId());
            menuNode.put("parentId", menu.getParentId());
            menuNode.put("title", menu.getMenuName());
            menuNode.put("spread", MENU_MULU.equals(menu.getMenuType()));
            menuNodes.add(menuNode);
        }
        return new Json(new LayUIPageVo(menuNodes.size(), menuNodes));
    }

    @Post("/listRoleMenuTree")
    public ActionResult listRoleMenuTree(@Param("roleId") Long roleId) {
        List<Long> menuIds = roleService.getMenuIdsByRoleId(roleId);
        List<SysMenu> menus = menuService.getMenus();
        List<Map<String, Object>> menuNodes = new ArrayList<>();
        for (SysMenu menu : menus) {
            Map<String, Object> menuNode = new HashMap<>();
            menuNode.put("id", menu.getMenuId());
            menuNode.put("parentId", menu.getParentId());
            menuNode.put("title", menu.getMenuName());
            menuNode.put("spread", MENU_MULU.equals(menu.getMenuType()));
            menuNode.put("checkArr", menuIds.contains(menu.getMenuId()) ? CHECKBOX_SELECT : CHECKBOX_NOT_SELECT);
            menuNodes.add(menuNode);
        }
        return new Json(new LayUIPageVo(menuNodes.size(), menuNodes));
    }

    @Put
    @HasPermission("system:menu:update")
    public ActionResult update(@Body SysMenu menu) {
        boolean bool = menuService.updateMenuById(menu);
        return RestResult.condition(bool, "操作成功", "操作失败");
    }

    @Post
    @HasPermission("system:menu:add")
    public ActionResult add(@Body SysMenu menu) {
        boolean bool = menuService.addMenu(menu);
        return RestResult.condition(bool, "添加成功", "添加失败");
    }

    @Delete("/{id}")
    @HasPermission("system:menu:del")
    public ActionResult delete(@Path("id") Long id) {
        boolean b = menuService.deleteMenuById(id);
        return RestResult.condition(b, "删除成功", "删除失败");
    }
}
