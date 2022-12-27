package top.huanyv.bean.utils;

import top.huanyv.bean.annotation.Order;

import java.util.Comparator;

/**
 * @author huanyv
 * @date 2022/12/27 15:29
 */
public class OrderUtil {

    public static class OrderAsc implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            Order order1 = o1.getClass().getAnnotation(Order.class);
            Order order2 = o2.getClass().getAnnotation(Order.class);
            if (order1 == null || order2 == null) {
                return 0;
            }
            return order1.value() - order2.value();
        }
    }

    public static class OrderDesc implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            Order order1 = o1.getClass().getAnnotation(Order.class);
            Order order2 = o2.getClass().getAnnotation(Order.class);
            if (order1 == null || order2 == null) {
                return 0;
            }
            return order2.value() - order1.value();
        }
    }
}
