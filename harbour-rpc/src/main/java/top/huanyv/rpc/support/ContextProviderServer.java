package top.huanyv.rpc.support;

import io.netty.channel.ChannelInboundHandlerAdapter;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.rpc.provider.NettyProviderServer;

/**
 * @author huanyv
 * @date 2023/1/21 16:32
 */
public class ContextProviderServer extends NettyProviderServer {

    private ApplicationContext context;

    public ContextProviderServer(String scanPackages, ApplicationContext context) {
        super(scanPackages);
        this.context = context;
    }

    @Override
    protected ChannelInboundHandlerAdapter getMsgHandler() {
        return new ContextProviderHandler(getServiceMapping(), context);
    }
}
