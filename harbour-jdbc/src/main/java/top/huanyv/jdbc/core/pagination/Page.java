package top.huanyv.jdbc.core.pagination;

import top.huanyv.bean.utils.JavaBean;

import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 16:28
 */
@JavaBean(false)
public class Page<T> {
    private int pageNum; // 当前页码
    private int pageSize; // 每页数据条数
    private int size; // 当前页面真实条数
    private long total; // 总数据数
    private int pages; // 总页码数
    private int prePage; // 上一页
    private int nextPage; // 下一页
    private List<T> data; // 数据

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        int pages = (int) (total / pageSize);
        if (total % pageSize > 0) {
            pages++;
        }
        int prePage = pageNum - 1;
        int nextPage = pageNum + 1;
        if (pageNum < 1) {
            pageNum = 1;
            prePage = 1;
            nextPage = pageNum + 1;
        } else if (pageNum > pages && pages > 0) {
            pageNum = pages;
            nextPage = pages;
            prePage = pageNum - 1;
        }
        this.pages = pages;
        this.prePage = prePage;
        this.nextPage = nextPage;
    }

    public int getPages() {
        return pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", total=" + total +
                ", pages=" + pages +
                ", prePage=" + prePage +
                ", nextPage=" + nextPage +
                ", data=" + data +
                '}';
    }
}
