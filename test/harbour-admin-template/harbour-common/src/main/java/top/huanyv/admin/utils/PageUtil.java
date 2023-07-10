package top.huanyv.admin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageUtil {

    public static <T> List<T> page(List<T> list, int pageNum, int pageSize) {
        if (list == null) {
            return Collections.emptyList();
        }

        int totalSize = list.size();
        int startIndex = (pageNum - 1) * pageSize;

        if (startIndex >= totalSize) {
            return Collections.emptyList();
        }

        int endIndex = Math.min(startIndex + pageSize, totalSize);

        return new ArrayList<>(list.subList(startIndex, endIndex));
    }

}