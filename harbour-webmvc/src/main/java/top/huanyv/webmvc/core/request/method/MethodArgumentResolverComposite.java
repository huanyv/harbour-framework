package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/11/14 20:30
 */
public class MethodArgumentResolverComposite implements MethodArgumentResolver {

    /**
     * 参数解析器列表
     */
    private List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();

    /**
     * 解析器缓存，提升获取速度
     */
    private Map<ClassDesc,MethodArgumentResolver> resolverCache = new ConcurrentHashMap<>(128);

    public MethodArgumentResolverComposite addResolver(MethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }

    public MethodArgumentResolverComposite addResolvers(List<MethodArgumentResolver> resolvers) {
        this.argumentResolvers.addAll(resolvers);
        return this;
    }

    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        MethodArgumentResolver argumentResolver = getArgumentResolver(methodParameterDesc);
        return argumentResolver.resolve(req, resp, methodParameterDesc);
    }

    @Override
    public boolean support(ClassDesc classDesc) {
        return getArgumentResolver(classDesc) != null;
    }

    public MethodArgumentResolver getArgumentResolver(ClassDesc classDesc) {
        MethodArgumentResolver resolver = this.resolverCache.get(classDesc);
        if (resolver == null) {
            for (MethodArgumentResolver argumentResolver : this.argumentResolvers) {
                if (argumentResolver.support(classDesc)) {
                    resolver = argumentResolver;
                    this.resolverCache.put(classDesc, resolver);
                    break;
                }
            }
        }
        return resolver;
    }

}
