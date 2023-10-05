package com.poc.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

public class NettyEchoClient {

    public static void main(String[] args) throws InterruptedException {
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            final Bootstrap bootstrap = new Bootstrap();
            final Bootstrap client = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(final Channel ch) {
                            ch.pipeline().addLast(
                                    new LoggingHandler(),
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new NettyEchoClientHandler()
                            );
                        }
                    });
            client.connect("localhost", 8080)
                    .channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
