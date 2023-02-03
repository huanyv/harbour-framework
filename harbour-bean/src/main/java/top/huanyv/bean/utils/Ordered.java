package top.huanyv.bean.utils;

/**
 * @author huanyv
 * @date 2022/12/27 15:22
 */
public interface Ordered {
    int HIGHEST_PRECEDENCE = -2147483648;
    int LOWEST_PRECEDENCE = 2147483647;

    default int getOrder() {
        return 0;
    }
}
