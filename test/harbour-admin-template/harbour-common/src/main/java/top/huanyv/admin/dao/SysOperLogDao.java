package top.huanyv.admin.dao;

import top.huanyv.admin.domain.dto.OperLogPageDto;
import top.huanyv.admin.domain.entity.SysOperLog;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/5/7 10:58
 */
public interface SysOperLogDao extends BaseDao<SysOperLog> {

    Page<SysOperLog> page(int pageNum, int pageSize, OperLogPageDto logPageDto);

    int clear();

    long countByDate(String date);
}
