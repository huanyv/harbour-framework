package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.domain.vo.dept.DeptSelectVo;
import top.huanyv.admin.service.SysDeptService;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.admin.utils.Util;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.security.HasPermission;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huanyv
 * @date 2023/3/27 21:18
 */
@Bean
@Route("/admin/dept")
public class SysDeptController {

    @Inject
    private SysDeptService deptService;

    @Get
    @HasPermission("system:dept:list")
    public ActionResult page(@Form PageDto pageDto, @Form SysDept dept) {
        Page<SysDept> page = deptService.page(pageDto.getPage(), pageDto.getLimit(), dept);
        return new LayUIPageVo(page.getTotal(), page.getData());
    }

    @Get("/listAll")
    @HasPermission("system:dept:list")
    public ActionResult list() {
        List<SysDept> sysDepts = deptService.listAll();
        List<DeptSelectVo> depts = sysDepts.stream()
                .map(dept -> new DeptSelectVo(dept.getDeptId(), dept.getDeptName()))
                .collect(Collectors.toList());
        return RestResult.ok("获取成功", depts);
    }

    @Post
    @HasPermission("system:dept:add")
    public ActionResult add(@Body SysDept dept) {
        boolean bool = deptService.addDept(dept);
        return RestResult.condition(bool, "添加成功", "添加失败");
    }

    @Put
    @HasPermission("system:dept:update")
    public ActionResult update(@Body SysDept dept) {
        boolean b = deptService.updateDeptById(dept);
        return RestResult.condition(b, "操作成功", "操作失败");
    }

    @Delete("/{ids}")
    @HasPermission("system:dept:del")
    public ActionResult removeUser(@Path("ids") String deptIds) {
        Long[] ids = Util.parseIds(deptIds);
        boolean b = deptService.deleteDeptByIds(ids);
        return RestResult.condition(b, "删除成功", "删除失败");
    }

}

