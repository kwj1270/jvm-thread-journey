package com.poc.nettyserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

//    private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            final DefaultFullHttpResponse response = new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            response.content().writeCharSequence("Hello World!", StandardCharsets.UTF_8);
            ctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
