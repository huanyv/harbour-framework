package top.huanyv.admin.dao.impl;

import top.huanyv.admin.dao.DeptDao;
import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.bean.ioc.FactoryBean;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.bean.utils.StringUtil;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/27 21:19
 */
@Dao
public class DeptDaoImpl implements DeptDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<SysDept> page(int pageNum, int pageSize, SysDept dept) {
        SqlBuilder sb = new SqlBuilder("select * from sys_dept")
                .condition("where", c -> c
                        .append(StringUtil.hasText(dept.getDeptName()), "dept_name like concat('%', ?, '%')", dept.getDeptName())
                        .and(StringUtil.hasText(dept.getAddress()), "address like concat('%', ?, '%')", dept.getAddress())
                        .and(StringUtil.hasText(dept.getPhonenumber()), "phonenumber like concat('%', ?, '%')", dept.getPhonenumber())
                );
        Page<SysDept> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysDept.class, sb.getSql(), sb.getArgs());
        return page;
    }
}
