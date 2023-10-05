package com.poc.study;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JavaNettyServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaNettyServerApplication.class);

    public static void main(String[] args) {
        final NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        final NioEventLoopGroup childGroup = new NioEventLoopGroup();

        final NioServerSocketChannel nioServerSocketChannel = new NioServerSocketChannel();
        parentGroup.register(nioServerSocketChannel);
        nioServerSocketChannel.pipeline().addLast(acceptor(childGroup));

        nioServerSocketChannel.bind(new InetSocketAddress(8080))
                .addListener(future -> {
                    if (future.isSuccess()) {
                        logger.info("Server bound to port 8080");
                    }
                });
    }

    private static ChannelHandler acceptor(final NioEventLoopGroup childGroup) {
        final DefaultEventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(4);
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
                logger.info("Acceptor.channelRead");
                if (msg instanceof SocketChannel) {
                    final SocketChannel socketChannel = (SocketChannel) msg;
                    socketChannel.pipeline().addLast(
                            eventExecutorGroup, new LoggingHandler(LogLevel.INFO)
                    );
                    socketChannel.pipeline().addLast(echoHandler());
                    childGroup.register(socketChannel);
                }
            }
        };
    }

    private static ChannelHandler echoHandler() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
                if (msg instanceof ByteBuf) {
                    try {
                        final ByteBuf buf = (ByteBuf) msg;
                        final int len = buf.readableBytes();
                        final Charset charset = StandardCharsets.UTF_8;
                        final CharSequence body = buf.readCharSequence(len, charset);
                        logger.info("EcoHandler channelRead: {}", body);

                        buf.readerIndex(0);
                        final ByteBuf result = buf.copy();
                        ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
                    } finally {
                        ReferenceCountUtil.release(msg);
                    }
                }
            }
        };
    }

}
