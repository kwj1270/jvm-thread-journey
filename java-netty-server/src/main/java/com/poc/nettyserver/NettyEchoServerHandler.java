
package com.poc.nettyserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyEchoServerHandler.class);

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        if (msg instanceof String) {
            ctx.writeAndFlush(msg)
                    .addListener(ChannelFutureListener.CLOSE);

            logger.info("Receive : {}", NettyEchoServerHandler.class);
        }
    }
}
