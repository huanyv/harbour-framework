package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.util.BaseDaoUtil;

import java.util.List;

/**
 * @author huanyv
 * @date 2022/8/31 19:58
 */
public interface BaseDao<T> {

    default List<T> selectAll() {
        Class<?> beanType = BaseDaoUtil.getType(this.getClass());


        return null;
    }

    default T selectById(Number id) {
        return null;
    }

    default int insert(Number id) {
        return 0;
    }

    default int updateById(Number id) {
        return 0;
    }

    default int deleteById(Number id) {
        return 0;
    }

}
