package top.huanyv.rpc.support;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.rpc.provider.RequestDTO;
import top.huanyv.rpc.provider.ServiceMapping;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ContextProviderHandler extends ChannelInboundHandlerAdapter {

    private final ServiceMapping serviceMapping;

    private final ApplicationContext context;

    public ContextProviderHandler(ServiceMapping serviceMapping, ApplicationContext context) {
        this.serviceMapping = serviceMapping;
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RequestDTO requestDTO = (RequestDTO) msg;
        Object result = new Object();

        String serviceName = requestDTO.getServiceName();
        if (serviceMapping.contains(serviceName)) {
            // 获取实例
            Object provider = null;
            Class<?> cls = serviceMapping.get(serviceName);
            try {
                provider = context.getBean(cls);
            } catch (NoSuchBeanDefinitionException e) {
                provider = serviceMapping.getInstance(cls);
            }
            Method method = cls.getMethod(requestDTO.getMethodName(), requestDTO.getMethodParameterTypes());
            // 方法执行
            result = method.invoke(provider, requestDTO.getMethodArgs());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

}
