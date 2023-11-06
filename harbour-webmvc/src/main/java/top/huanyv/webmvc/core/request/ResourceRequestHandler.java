package top.huanyv.webmvc.core.request;

import top.huanyv.bean.utils.IoUtil;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.resource.ResourceHandler;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;

/**
 * @author huanyv
 * @date 2023/2/17 15:55
 */
public class ResourceRequestHandler implements RequestHandler {

    private final ResourceHandler resourceHandler;

    public ResourceRequestHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        InputStream inputStream = resourceHandler.getInputStream(req.raw());
        if (inputStream != null) {
            String mimeType = req.ctx().getMimeType(req.getUri());
            resp.contentType(mimeType);
            ServletOutputStream outputStream = resp.getOutputStream();
            IoUtil.copy(inputStream, outputStream);
            IoUtil.close(inputStream, outputStream);
        } else {
            // 静态资源不存在
            resp.error(404, "Resources not found.");
        }
    }

    @Override
    public String toString() {
        return resourceHandler.toString();
    }
}
