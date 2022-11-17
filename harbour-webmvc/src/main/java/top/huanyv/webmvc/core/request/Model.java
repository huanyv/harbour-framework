package top.huanyv.webmvc.core.request;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/17 16:21
 */
public class Model {

    private HttpServletRequest request;

    public Model(HttpServletRequest request) {
        this.request = request;
    }

    public Object get(String name) {
        return request.getAttribute(name);
    }

    public Model add(String name, Object value) {
        request.setAttribute(name, value);
        return this;
    }

    public void remove(String name) {
        request.removeAttribute(name);
    }

    public String[] getNames() {
        List<String> names = new ArrayList<>();
        Enumeration<String> attributeames = request.getAttributeNames();
        while (attributeames.hasMoreElements()) {
            names.add(attributeames.nextElement());
        }
        return names.toArray(new String[0]);
    }

}
