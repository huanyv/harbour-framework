package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.SysOperLogDao;
import top.huanyv.admin.domain.dto.OperLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.admin.service.SysOperLogService;
import top.huanyv.admin.utils.PageDto;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;

/**
 * @author huanyv
 * @date 2023/5/7 17:09
 */
@Component
public class SysOperLogServiceImpl implements SysOperLogService {

    @Inject
    private SysOperLogDao operLogDao;

    @Override
    public Page<SysOperLog> page(PageDto pageDto, OperLogPageDto logPageDto) {
        return operLogDao.page(pageDto.getPage(), pageDto.getLimit(), logPageDto);
    }

    @Override
    public boolean insert(SysOperLog operLog) {
        return operLogDao.insert(operLog) > 0;
    }


    @Override
    public boolean deleteByIds(Long[] ids) {
        for (Long id : ids) {
            int i = operLogDao.deleteById(id);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean clear() {
        return operLogDao.clear() > 0;
    }

}
