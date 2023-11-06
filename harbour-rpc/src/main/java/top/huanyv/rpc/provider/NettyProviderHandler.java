package top.huanyv.rpc.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NettyProviderHandler extends ChannelInboundHandlerAdapter {


    private final ServiceMapping serviceMapping;

    public NettyProviderHandler(ServiceMapping serviceMapping) {
        this.serviceMapping = serviceMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RequestDTO requestDTO = (RequestDTO) msg;
        Object result = new Object();

        String serviceName = requestDTO.getServiceName();
        if (serviceMapping.contains(serviceName)) {
            Class<?> cls = this.serviceMapping.get(serviceName);
            // 获取实例
            Object provider = serviceMapping.getInstance(cls);

            Method method = cls.getMethod(requestDTO.getMethodName(), requestDTO.getMethodParameterTypes());
            result = method.invoke(provider, requestDTO.getMethodArgs());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

}
