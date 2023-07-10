package top.huanyv.admin.service;

import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/27 21:20
 */
public interface SysDeptService {

    Page<SysDept> page(int pageNum, int pageSize, SysDept dept);

    List<SysDept> listAll();

    boolean addDept(SysDept dept);

    boolean updateDeptById(SysDept dept);

    boolean deleteDeptByIds(Long[] ids);
}
