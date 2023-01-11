package top.huanyv.jdbc.builder;

import java.util.*;

/**
 * @author huanyv
 * @date 2022/8/31 19:58
 */
public interface BaseDao<T> {

    /**
     * 查询所有
     *
     * @return list
     */
    default List<T> selectAll() {
        return null;
    }

    /**
     * 根据ID查询一个对象
     *
     * @param id id
     * @return T
     */
    default T selectById(Number id) {
        return null;
    }

    /**
     * 根据对象实体，插入一条数据
     *
     * @param t T
     * @return 插入成功的条数
     */
    default int insert(T t) {
        return 0;
    }

    /**
     * 根据实体对象，用ID更新一条数据。只更新属性不为 null 的字段，ID不可为空
     *
     * @param t T
     * @return 更新成功的条数
     */
    default int updateById(T t) {
        return 0;
    }

    /**
     * 根据ID删除一条数据
     *
     * @param id ID
     * @return 删除成功的条数
     */
    default int deleteById(Number id) {
        return 0;
    }

}
