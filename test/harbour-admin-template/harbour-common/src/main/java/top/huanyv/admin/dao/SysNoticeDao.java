package top.huanyv.admin.dao;

import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/30 21:11
 */
public interface SysNoticeDao extends BaseDao<SysNotice> {

    Page<SysNotice> page(int pageNum, int pageSize, SysNotice notice);

    List<SysNotice> getNewNotice();

}
