package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.SysNoticeDao;
import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.admin.service.SysNoticeService;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/30 21:14
 */
@Component
public class SysNoticeServiceImpl implements SysNoticeService {

    @Inject
    private SysNoticeDao noticeDao;

    @Override
    public Page<SysNotice> page(int pageNum, int pageSize, SysNotice notice) {
        return noticeDao.page(pageNum, pageSize, notice);
    }

    @Override
    public List<SysNotice> getNewNotice() {
        return noticeDao.getNewNotice();
    }

    @Override
    public boolean addNotice(SysNotice notice) {
        String loginUsername = LoginUtil.getLoginUsername();
        notice.setCreateBy(loginUsername);
        notice.setCreateTime(new Date());
        return noticeDao.insert(notice) > 0;
    }

    @Override
    public boolean updateNoticeById(SysNotice notice) {
        String loginUsername = LoginUtil.getLoginUsername();
        notice.setUpdateBy(loginUsername);
        notice.setUpdateTime(new Date());
        return noticeDao.updateById(notice) > 0;
    }

    @Override
    public boolean deleteNoticeById(Long[] ids) {
        for (Long id : ids) {
            int i = noticeDao.deleteById(id);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }


}
