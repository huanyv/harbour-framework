package top.huanyv.jdbc.core.pagination;

import top.huanyv.bean.utils.Assert;

/**
 * @author huanyv
 * @date 2023/1/9 17:21
 */
public final class PageHolder {
    private PageHolder() {
    }

    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static Page startPage(int pageNum, int pageSize) {
        Page page = new Page(pageNum, pageSize);
        pageThreadLocal.set(page);
        return page;
    }

    public static Page getPage() {
        Page page = pageThreadLocal.get();
        Assert.notNull(page, "page info not set!");
        return page;
    }

    public static void remove() {
        pageThreadLocal.remove();
    }

}
