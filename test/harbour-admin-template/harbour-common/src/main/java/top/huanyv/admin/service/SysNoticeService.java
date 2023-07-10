package top.huanyv.admin.service;

import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.jdbc.core.pagination.Page;

import javax.management.Notification;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/30 21:14
 */
public interface SysNoticeService {

    Page<SysNotice> page(int pageNum, int pageSize, SysNotice notice);

    List<SysNotice> getNewNotice();

    boolean addNotice(SysNotice notice);

    boolean updateNoticeById(SysNotice notice);

    boolean deleteNoticeById(Long[] ids);

}
