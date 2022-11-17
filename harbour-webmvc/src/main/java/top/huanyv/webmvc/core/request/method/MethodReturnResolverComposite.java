package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/11/15 16:29
 */
public class MethodReturnResolverComposite implements MethodReturnResolver {

    private Set<MethodReturnResolver> returnResolvers = new HashSet<>();

    private Map<Method, MethodReturnResolver> resolverCache = new ConcurrentHashMap<>(64);

    public MethodReturnResolverComposite() {
        returnResolvers.add(new BodyMethodReturnResolver());
        returnResolvers.add(new ViewMethodReturnResolver());
    }

    @Override
    public void resolve(HttpRequest req, HttpResponse resp, Object returnValue, Method method) throws ServletException, IOException {
        MethodReturnResolver returnResolver = getReturnResolver(method);
        returnResolver.resolve(req, resp, returnValue, method);
    }

    @Override
    public boolean support(Method method) {
        return getReturnResolver(method) != null;
    }

    public MethodReturnResolver getReturnResolver(Method method) {
        MethodReturnResolver result = this.resolverCache.get(method);
        for (MethodReturnResolver resolver : this.returnResolvers) {
            if (resolver.support(method)) {
                result = resolver;
                this.resolverCache.put(method, resolver);
                break;
            }
        }
        return result;
    }

}
