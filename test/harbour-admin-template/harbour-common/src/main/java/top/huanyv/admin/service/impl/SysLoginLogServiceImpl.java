package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.SysLoginLogDao;
import top.huanyv.admin.domain.dto.LoginLogPageDto;
import top.huanyv.admin.domain.entity.SysLoginLog;
import top.huanyv.admin.service.SysLoginLogService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;

import java.text.ParseException;

/**
 * @author huanyv
 * @date 2023/4/22 13:47
 */
@Component
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Inject
    private SysLoginLogDao sysLoginLogDao;

    @Override
    public Page<SysLoginLog> page(int pageNum, int pageSize, LoginLogPageDto logPageDto) throws ParseException {
        return sysLoginLogDao.page(pageNum, pageSize, logPageDto);
    }

    @Override
    public boolean insert(SysLoginLog loginLog) {
        return sysLoginLogDao.insert(loginLog) > 0;
    }

    @Override
    public boolean deleteByIds(Long[] ids) {
        for (Long id : ids) {
            int i = sysLoginLogDao.deleteById(id);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean clear() {
        return sysLoginLogDao.clear() > 0;
    }
}
