package top.huanyv.admin.service;

import top.huanyv.admin.domain.dto.LoginLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.jdbc.core.pagination.Page;

import java.text.ParseException;

/**
 * @author huanyv
 * @date 2023/4/22 13:46
 */
public interface SysLoginLogService {

    Page<SysLoginLog> page(int pageNum, int pageSize, LoginLogPageDto logPageDto) throws ParseException;

    boolean insert(SysLoginLog loginLog) ;

    boolean deleteByIds(Long[] ids);

    boolean clear();
}
