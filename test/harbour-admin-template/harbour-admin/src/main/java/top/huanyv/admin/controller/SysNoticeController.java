package top.huanyv.admin.controller;

import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.service.SysNoticeService;
import top.huanyv.admin.service.SysRoleService;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.admin.utils.Util;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/30 21:15
 */
@Bean
@Route("/admin/notice")
public class SysNoticeController {

    @Inject
    private SysNoticeService sysNoticeService;

    @Get
    public ActionResult page(@Form PageDto pageDto, @Form SysNotice sysNotice) {
        Page<SysNotice> page = sysNoticeService.page(pageDto.getPage(), pageDto.getLimit(), sysNotice);
        return new LayUIPageVo(page.getTotal(), page.getData());
    }

    @Post
    public ActionResult add(@Body SysNotice notice) {
        boolean bool = sysNoticeService.addNotice(notice);
        return RestResult.condition(bool, "添加成功", "添加失败");
    }

    @Put
    public ActionResult update(@Body SysNotice notice) {
        boolean b = sysNoticeService.updateNoticeById(notice);
        return RestResult.condition(b, "操作成功", "操作失败");
    }

    @Delete("/{ids}")
    public ActionResult removeUser(@Path("ids") String noticeIds) {
        Long[] ids = Util.parseIds(noticeIds);
        boolean b = sysNoticeService.deleteNoticeById(ids);
        return RestResult.condition(b, "删除成功", "删除失败");
    }
}
