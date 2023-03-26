package top.huanyv.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.huanyv.rpc.provider.RequestDTO;
import top.huanyv.rpc.util.Address;

public class NettyConsumerServer implements ConsumerServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyConsumerServer.class);

    public Object execute(String serviceAddress, RequestDTO requestDTO, int timeout) {
        Address address = Address.parse(serviceAddress);
        String host = address.getIp();
        int port = address.getPort();

        NettyConsumerHandler consumerHandler = new NettyConsumerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new IdleStateHandler(0 ,0 , timeout));
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(ConsumerProxy.class.getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(consumerHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();

            Channel channel = future.channel();
            channel.writeAndFlush(requestDTO);
            logger.info("Send request:{}", requestDTO);
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
        return consumerHandler.getResponse();

    }
}
