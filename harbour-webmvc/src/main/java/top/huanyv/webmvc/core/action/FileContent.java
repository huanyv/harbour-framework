package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import java.io.File;

/**
 * @author huanyv
 * @date 2023/2/13 16:34
 */
public class FileContent implements ActionResult {

    private String fileName;

    private File file;

    public FileContent(java.io.File file) {
        this("", file);
    }

    public FileContent(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        resp.file(fileName, file);
    }

}
