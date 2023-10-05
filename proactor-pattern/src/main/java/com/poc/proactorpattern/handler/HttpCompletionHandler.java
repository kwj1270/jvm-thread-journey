package com.poc.proactorpattern.handler;

import com.poc.proactorpattern.util.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class HttpCompletionHandler implements CompletionHandler<Integer, Void> {

    private static final Logger logger = LoggerFactory.getLogger(HttpCompletionHandler.class);


    private final MessageCodec messageCodec;
    private final ByteBuffer requestByteBuffer;
    private final AsynchronousSocketChannel socketChannel;

    public HttpCompletionHandler(final AsynchronousSocketChannel socketChannel) {
        this.messageCodec = new MessageCodec();
        this.requestByteBuffer = ByteBuffer.allocateDirect(1024);
        this.socketChannel = socketChannel;
        this.socketChannel.read(this.requestByteBuffer, null, this);
    }

    @Override
    public void completed(final Integer result, final Void attachment) {
        final String requestBody = handleRequest();
        logger.info("Request Body = {}", requestBody);
        handleResponse(requestBody);
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        logger.error("Failed to Read From Client = {}", exc);
    }

    private String handleRequest() {
        return messageCodec.decode(this.requestByteBuffer);
    }

    private void handleResponse(final String requestBody) {
        final ByteBuffer responseByteBuffer = messageCodec.encode(requestBody);
        socketChannel.write(responseByteBuffer, null, new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final Object attachment) {
                closeSocket();
            }

            @Override
            public void failed(final Throwable exc, final Object attachment) {
                closeSocket();
            }

            private void closeSocket() {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
