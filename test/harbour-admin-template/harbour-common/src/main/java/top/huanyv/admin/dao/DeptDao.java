package top.huanyv.admin.dao;

import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

/**
 * @author huanyv
 * @date 2023/3/27 21:15
 */
public interface DeptDao extends BaseDao<SysDept> {

    Page<SysDept> page(int pageNum, int pageSize, SysDept dept);

}
