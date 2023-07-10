package top.huanyv.admin.dao.impl;

import top.huanyv.admin.dao.SysNoticeDao;
import top.huanyv.admin.domain.entity.SysNotice;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.tools.utils.StringUtil;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/30 21:12
 */
@Dao
public class SysNoticeDaoImpl implements SysNoticeDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<SysNotice> page(int pageNum, int pageSize, SysNotice notice) {
        SqlBuilder sb = new SqlBuilder("select * from sys_notice")
                .condition("where", c -> c
                    .append(StringUtil.hasText(notice.getTitle()), "title like concat('%', ?, '%')", notice.getTitle())
                    .and(StringUtil.hasText(notice.getContent()), "content like concat('%', ?, '%')", notice.getContent())
                );
        Page<SysNotice> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysNotice.class, sb.getSql(), sb.getArgs());
        return page;
    }

    @Override
    public List<SysNotice> getNewNotice() {
        String sql = "select * from sys_notice order by is_top asc, create_time desc, notice_id desc limit 10";
        return sqlContext.selectList(SysNotice.class, sql);
    }
}
