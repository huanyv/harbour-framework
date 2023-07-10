package top.huanyv.admin.dao;

import top.huanyv.admin.domain.dto.LoginLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

import java.text.ParseException;

/**
 * @author huanyv
 * @date 2023/4/22 13:48
 */
public interface SysLoginLogDao extends BaseDao<SysLoginLog> {

    Page<SysLoginLog> page(int pageNum, int pageSize, LoginLogPageDto logPageDto) throws ParseException;

    int clear();
}
