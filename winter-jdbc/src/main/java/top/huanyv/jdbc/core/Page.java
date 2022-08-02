package top.huanyv.jdbc.core;

import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 16:28
 */
public class Page<T> {
    private int pageNum; // 当前页码
    private int pageSize; // 每页数据条数
    private int size; // 当前页面真实条数
    private long total; // 总数据数
    private int pages; // 总页码数
    private int prePage; // 上一页
    private int nextPage; // 下一页
    private List<T> data; // 数据

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
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
