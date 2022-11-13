package top.huanyv.webmvc.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/15 19:08
 */
public class ResourceHolderManager implements ResourceHolder{

    private List<ResourceHolder> resourceHolders;

    public ResourceHolderManager() {
        resourceHolders = new ArrayList<>();
        resourceHolders.add(new ClassPathResourceHolder());
        resourceHolders.add(new FileSystemResourceHolder());
    }

    @Override
    public InputStream getInputStream(String location) {
        for (ResourceHolder holder : resourceHolders) {
            if (holder.isMatch(location)) {
                return holder.getInputStream(location);
            }
        }
        return null;
    }

    @Override
    public boolean isMatch(String location) {
        return getInputStream(location) != null;
    }
}
