package top.huanyv.web.guard;

import org.junit.Test;
import top.huanyv.webmvc.guard.NavigationGuardMapping;

public class NavigationGuardMappingTest {

    @Test
    public void hasUrlPatten() {
        NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
        navigationGuardMapping.setUrlPatterns(new String[] {"/**"});
        navigationGuardMapping.setExcludeUrl(new String[]{"/static/**"});
        boolean b = navigationGuardMapping.hasUrlPatten("/");
        boolean b1 = navigationGuardMapping.isExclude("/");
        System.out.println(b);
        System.out.println(b1);
    }
}