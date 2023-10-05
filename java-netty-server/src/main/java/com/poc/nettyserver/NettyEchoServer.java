package com.poc.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyEchoServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyEchoServer.class);

    public static void main(String[] args) throws InterruptedException {
        final NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        final NioEventLoopGroup childGroup = new NioEventLoopGroup();
        final DefaultEventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(5);

        try {

            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            final ServerBootstrap server = serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(final Channel ch) {
                            ch.pipeline().addLast(eventExecutorGroup, new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new NettyEchoServerHandler()
                            );
                        }
                    });
            server.bind(8080).sync()
                    .addListener((FutureListener) future -> {
                        if (future.isSuccess()) {
                            logger.info("Success to bind 8080");
                        } else {
                            logger.error("Fail to bind 8080");
                        }
                    }).channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            eventExecutorGroup.shutdownGracefully();
        }
    }
}
