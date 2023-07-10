package top.huanyv.admin.service;

import top.huanyv.admin.domain.dto.OperLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.jdbc.core.pagination.Page;

/**
 * @author huanyv
 * @date 2023/5/7 17:09
 */
public interface SysOperLogService {

    Page<SysOperLog> page(PageDto pageDto, OperLogPageDto logPageDto);

    boolean insert(SysOperLog operLog) ;

    boolean deleteByIds(Long[] ids);

    boolean clear();
}
