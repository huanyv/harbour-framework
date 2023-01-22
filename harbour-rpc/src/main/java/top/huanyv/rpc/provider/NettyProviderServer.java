package top.huanyv.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import top.huanyv.rpc.annotation.Provider;
import top.huanyv.rpc.register.Registry;
import top.huanyv.rpc.util.Address;
import top.huanyv.rpc.util.RpcConfigurer;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NettyProviderServer implements ProviderServer {

    private String scanPackages;

    private final ServiceMapping serviceMapping;

    private final Registry registry;

    public NettyProviderServer() {
        this.registry = RpcConfigurer.create().getRegistry();
        this.serviceMapping = new ServiceMapping();
    }

    public NettyProviderServer(String scanPackages) {
        this();
        this.scanPackages = scanPackages;
    }

    public void start(String selfAddress) {
        // 注册服务
        Set<Class<?>> classes = ClassUtil.getClasses(scanPackages);
        for (Class<?> cls : classes) {
            Provider providerAnnotation = cls.getAnnotation(Provider.class);
            if (providerAnnotation != null) {
                String serviceName = providerAnnotation.value();
                serviceName = StringUtil.hasText(serviceName) ? serviceName : providerAnnotation.interfaceClass().getName();
                serviceMapping.add(serviceName, cls);
                // 注册到注册中心
                registry.register(selfAddress, serviceName);
            }
        }

        Address address = Address.parse(selfAddress);
        publisher(address.getIp(), address.getPort());
    }

    private void publisher(String ip, Integer port) {
        // 启动服务
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            pipeline.addLast(getMsgHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
        }
    }

    protected ChannelInboundHandlerAdapter getMsgHandler() {
        return new NettyProviderHandler(getServiceMapping());
    }

    public ServiceMapping getServiceMapping() {
        return serviceMapping;
    }
}
