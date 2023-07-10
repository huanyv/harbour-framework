package top.huanyv.admin.controller;

import top.huanyv.admin.domain.dto.LoginLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.service.SysLoginLogService;
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
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.action.ActionResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author huanyv
 * @date 2023/4/23 17:19
 */
@Component
@Route("/admin/monitor/login")
public class SysLoginLogController {

    @Inject
    private SysLoginLogService loginLogService;

    @Get
    public ActionResult page(@Form PageDto pageDto, @Form LoginLogPageDto loginLog) throws ParseException {
        Page<SysLoginLog> page = loginLogService.page(pageDto.getPage(), pageDto.getLimit(), loginLog);
        return new LayUIPageVo(page.getTotal(), page.getData());
    }

    @Delete("/{ids}")
    public ActionResult delete(@Path("ids") String ids) {
        Long[] parseIds = Util.parseIds(ids);
        boolean bool = loginLogService.deleteByIds(parseIds);
        return RestResult.condition(bool, "删除成功", "删除失败");
    }

    @Delete("/clear")
    public ActionResult clear() {
        boolean clear = loginLogService.clear();
        return RestResult.condition(clear, "清除成功", "清除失败");
    }

}
