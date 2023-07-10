package top.huanyv.admin.dao.impl;

import top.huanyv.admin.dao.SysLoginLogDao;
import top.huanyv.admin.domain.dto.LoginLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.tools.utils.StringUtil;

import java.text.ParseException;

/**
 * @author huanyv
 * @date 2023/4/22 13:48
 */
@Dao
public class SysLoginLogDaoImpl implements SysLoginLogDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<SysLoginLog> page(int pageNum, int pageSize, LoginLogPageDto logPageDto) throws ParseException {
        String startDate = "";
        String endDate = "";
        if (StringUtil.hasText(logPageDto.getLoginDateRange())) {
            String[] range = logPageDto.getLoginDateRange().split(" - ");
            startDate = range[0];
            endDate = range[1];
        }
        String finalEndDate = endDate;
        String finalStartDate = startDate;
        SqlBuilder sb = new SqlBuilder("select * from sys_login_log").condition("where", c -> c
                .and(StringUtil.hasText(logPageDto.getLoginUser()), "login_user like concat('%', ?, '%')", logPageDto.getLoginUser())
                .and(StringUtil.hasText(logPageDto.getStatus()), "status = ?", logPageDto.getStatus())
                .and(StringUtil.hasText(logPageDto.getLoginAddr()), "login_addr like concat('%', ?, '%')", logPageDto.getLoginAddr())
                .and(StringUtil.hasText(logPageDto.getLoginDateRange()), "login_date between ? and ?", finalStartDate, finalEndDate)
        ).append("order by login_date desc");
        Page<SysLoginLog> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysLoginLog.class, sb.getSql(), sb.getArgs());
        return page;
    }


    public int clear() {
        String sql = "delete from sys_login_log";
        return sqlContext.update(sql);
    }
}
