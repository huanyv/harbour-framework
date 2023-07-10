package top.huanyv.admin.dao.impl;

import top.huanyv.admin.dao.SysOperLogDao;
import top.huanyv.admin.domain.dto.OperLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.tools.utils.StringUtil;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/5/7 17:01
 */
@Dao
public class SysOperLogDaoImpl implements SysOperLogDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<SysOperLog> page(int pageNum, int pageSize, OperLogPageDto logPageDto) {
        String startDate = "";
        String endDate = "";
        if (StringUtil.hasText(logPageDto.getOperDateRange())) {
            String[] range = logPageDto.getOperDateRange().split(" - ");
            startDate = range[0];
            endDate = range[1];
        }
        String finalEndDate = endDate;
        String finalStartDate = startDate;
        SqlBuilder sb = new SqlBuilder("select * from sys_oper_log").condition("where", c -> c
                .and(StringUtil.hasText(logPageDto.getOperUser()), "oper_user like concat('%', ?, '%')", logPageDto.getOperUser())
                .and(StringUtil.hasText(logPageDto.getOperAddr()), "oper_addr like concat('%', ?, '%')", logPageDto.getOperAddr())
                .and(StringUtil.hasText(logPageDto.getOperDateRange()), "oper_date between ? and ?", finalStartDate, finalEndDate)
        ).append("order by oper_date desc");
        Page<SysOperLog> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysOperLog.class, sb.getSql(), sb.getArgs());
        return page;
    }

    @Override
    public int clear() {
        String sql = "delete from sys_oper_log";
        return sqlContext.update(sql);
    }

    @Override
    public long countByDate(String date) {
        String sql = "select distinct oper_user from sys_oper_log where date_format(oper_date, '%Y-%m-%d')=?";
        return sqlContext.selectCount(sql, date);
    }
}
