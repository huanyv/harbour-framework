package top.huanyv.admin.domain.vo.cache;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author huanyv
 * @date 2023/4/23 19:10
 */
@Data
public class CacheNode {
    private int id;

    private int parentId;

    private String title;
    private List<CacheNode> children = new ArrayList<>();

    public CacheNode(int id, int parentId, String title) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
    }

    public CacheNode(String name, List<CacheNode> child) {
        this.title = name;
        this.children = child;
    }

    public CacheNode(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CacheNode> getChildren() {
        return children;
    }

    public void setChildren(List<CacheNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheNode cacheNode = (CacheNode) o;
        return Objects.equals(title, cacheNode.title) && Objects.equals(children, cacheNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, children);
    }

}
