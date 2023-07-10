package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.SysOperLogDao;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.admin.service.SysEChartsService;
import top.huanyv.admin.service.SysOperLogService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

/**
 * @author huanyv
 * @date 2023/5/7 21:36
 */
@Component
public class SysEChartsServiceImpl implements SysEChartsService {

    @Inject
    private SysOperLogDao operLogDao;

    @Override
    public Map<String, Long> weeks() {
        Map<String, Long> map = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            String today = formatter.format(date);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String weekName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA);
            long count = operLogDao.countByDate(today);
            map.put(weekName, count);
        }
        return map;
    }
}
