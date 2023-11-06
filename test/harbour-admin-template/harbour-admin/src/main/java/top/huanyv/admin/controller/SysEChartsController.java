package top.huanyv.admin.controller;

import sun.java2d.pipe.RegionSpanIterator;
import top.huanyv.admin.service.SysEChartsService;
import top.huanyv.admin.service.SysOperLogService;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.core.action.ActionResult;

import java.util.Map;

/**
 * @author huanyv
 * @date 2023/5/7 21:33
 */
@Bean
@Route("/admin/echarts")
public class SysEChartsController {

    @Inject
    private SysEChartsService eChartsService;

    @Get("/weeks")
    public ActionResult weeks() {
        Map<String, Long> weeks = eChartsService.weeks();
        return RestResult.ok("获取成功", weeks);
    }

}
