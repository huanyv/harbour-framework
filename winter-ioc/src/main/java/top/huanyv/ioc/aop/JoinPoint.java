package top.huanyv.ioc.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/9/13 10:53
 */
public class JoinPoint {

    private Object target;

    private Method method;

    private Object[] args;

    // 切面执行链
    private List<AspectAdvice> chain = new ArrayList<>();

    // 当前切面
    private AspectAdvice curAdvice;

    private int index = 1;

    public void addAspect(List<AspectAdvice> list) {
        chain.addAll(list);
//        curAdvice = chain.get(0);
    }


    private Object handle() throws InvocationTargetException, IllegalAccessException {
        if (index >= chain.size()) {
            // 方法执行
            return method.invoke(target, args);
        }
        this.curAdvice = this.chain.get(index);
        index++;
        return this.curAdvice.aroundAdvice(this);
    }


    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        AspectAdvice aspectAdvice = this.curAdvice;
        aspectAdvice.beforeAdvice(args);
        Object result = this.handle();
        aspectAdvice.afterAdvice(args, result);
        return result;
    }

    /**
     * 向链头发请求
     *
     * @return {@link Object}
     */
    public Object run() throws InvocationTargetException, IllegalAccessException {
        this.curAdvice = this.chain.get(0);
        return this.curAdvice.aroundAdvice(this);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setChain(List<AspectAdvice> chain) {
        this.chain = chain;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
