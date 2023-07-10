package top.huanyv.admin.domain.vo;

import lombok.Data;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.action.Json;

/**
 * @author huanyv
 * @date 2023/2/22 13:35
 */
@Data
public class LayUIPageVo implements ActionResult {
    private int code = 0;
    private String msg = "";
    private long count;
    private Object data;

    public LayUIPageVo(long count, Object data) {
        this.count = count;
        this.data = data;
    }

    public LayUIPageVo(Page<?> page) {
        this(page.getTotal(), page.getData());
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        new Json(this).execute(req, resp);
    }
}
