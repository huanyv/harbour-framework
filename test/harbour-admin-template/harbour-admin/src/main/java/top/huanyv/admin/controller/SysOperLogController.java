package top.huanyv.admin.controller;

import top.huanyv.admin.domain.dto.OperLogPageDto;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.service.SysOperLogService;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.admin.utils.Util;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.annotation.Delete;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;

/**
 * @author huanyv
 * @date 2023/5/7 17:07
 */
@Component
@Route("/admin/monitor/oper")
public class SysOperLogController {

    @Inject
    private SysOperLogService operLogService;

    @Get
    public LayUIPageVo page(@Form PageDto pageDto, @Form OperLogPageDto logPageDto) {
        Page<SysOperLog> page = operLogService.page(pageDto, logPageDto);
        return new LayUIPageVo(page.getTotal(), page.getData());
    }

    @Delete("/{ids}")
    public ActionResult delete(@Path("ids") String ids) {
        Long[] parseIds = Util.parseIds(ids);
        boolean bool = operLogService.deleteByIds(parseIds);
        return RestResult.condition(bool, "删除成功", "删除失败");
    }

    @Delete("/clear")
    public ActionResult clear() {
        boolean clear = operLogService.clear();
        return RestResult.condition(clear, "清除成功", "清除失败");
    }

}
