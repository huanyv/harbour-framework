package top.huanyv.webmvc.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/4 15:04
 */
public class ResourceMapping {

    private String urlPattern;

    private List<String> locations = new ArrayList<>();

    public ResourceMapping addResourceLocations(String... locations) {
        this.locations.addAll(Arrays.asList(locations));
        return this;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    @Override
    public String toString() {
        return "ResourceMapping{" +
                "urlPattern='" + urlPattern + '\'' +
                ", locations=" + locations +
                '}';
    }
}
