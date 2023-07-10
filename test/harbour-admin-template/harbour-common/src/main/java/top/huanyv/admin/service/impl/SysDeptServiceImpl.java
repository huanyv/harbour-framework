package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.DeptDao;
import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.admin.service.SysDeptService;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/27 21:20
 */
@Component
public class SysDeptServiceImpl implements SysDeptService {

    @Inject
    private DeptDao deptDao;

    @Override
    public Page<SysDept> page(int pageNum, int pageSize, SysDept dept) {
        return deptDao.page(pageNum, pageSize, dept);
    }

    @Override
    public List<SysDept> listAll() {
        return deptDao.selectAll();
    }

    @Override
    public boolean addDept(SysDept dept) {
        String loginUsername = LoginUtil.getLoginUsername();
        dept.setCreateBy(loginUsername);
        dept.setCreateTime(new Date());
        return deptDao.insert(dept) > 0;
    }

    @Override
    public boolean updateDeptById(SysDept dept) {
        String loginUsername = LoginUtil.getLoginUsername();
        dept.setUpdateBy(loginUsername);
        dept.setUpdateTime(new Date());
        return deptDao.updateById(dept) > 0;
    }

    @Override
    public boolean deleteDeptByIds(Long[] ids) {
        for (Long id : ids) {
            int i = deptDao.deleteById(id);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }
}
