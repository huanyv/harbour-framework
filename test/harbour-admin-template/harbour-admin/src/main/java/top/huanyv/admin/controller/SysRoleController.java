package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.domain.vo.SysRolesLoadVo;
import top.huanyv.admin.service.SysRoleService;
import top.huanyv.admin.utils.Constants;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.admin.utils.Util;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.security.HasPermission;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/3/15 20:27
 */
@Component
@Route("/admin/role")
public class SysRoleController {

    @Inject
    private SysRoleService sysRoleService;

    @Get
    @HasPermission("system:role:list")
    public ActionResult page(@Form PageDto pageDto, @Form SysRole role, HttpSession session) {
        Page<SysRole> page = sysRoleService.getPage(pageDto.getPage(), pageDto.getLimit(), role);
        return new LayUIPageVo(page.getTotal(), page.getData());
    }

    @Get("/loadRolesByUserId")
    public ActionResult loadRoles(@Param("userId") Long userId) {
        List<SysRole> list = sysRoleService.list();
        List<SysRolesLoadVo> vos = BeanUtil.copyBeanList(list, SysRolesLoadVo.class);
        List<SysRole> sysRoles = sysRoleService.listRoleByUserId(userId);
        Set<Long> roleIds = sysRoles.stream().map(role -> role.getRoleId()).collect(Collectors.toSet());
        for (SysRolesLoadVo vo : vos) {
            vo.setLAY_CHECKED(roleIds.contains(vo.getRoleId()));
        }
        return new LayUIPageVo(0, vos);
    }

    @Post
    @HasPermission("system:role:add")
    public ActionResult add(@Body SysRole role) {
        boolean bool = sysRoleService.add(role);
        return RestResult.condition(bool, "添加成功", "添加失败");
    }

    @Put
    @HasPermission("system:role:update")
    public ActionResult update(@Body SysRole role) {
        boolean b = sysRoleService.updateRoleById(role);
        return RestResult.condition(b, "操作成功", "操作失败");
    }

    @Delete("/{ids}")
    @HasPermission("system:role:del")
    public ActionResult removeUser(@Path("ids") String ids) {
        boolean b = sysRoleService.deleteRoleById(ids);
        return RestResult.condition(b, "删除成功", "删除失败");
    }

    @Post("/updateRoleMenus")
    public ActionResult updateRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") String menuIds) {
        Long[] ids = Util.parseIds(menuIds);
        boolean bool = sysRoleService.updateRoleMenu(roleId, ids);
        return RestResult.condition(bool, "更新成功", "更新失败");
    }

}
