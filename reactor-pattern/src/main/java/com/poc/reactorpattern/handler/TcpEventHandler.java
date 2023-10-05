package com.poc.reactorpattern.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpEventHandler implements EventHandler {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);
    private static final Logger logger = LoggerFactory.getLogger(TcpEventHandler.class);

    private final Selector selector;
    private final SocketChannel socketChannel;

    public TcpEventHandler(final Selector selector, final SocketChannel socketChannel) {
        try {
            this.selector = selector;
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            this.socketChannel.register(selector, SelectionKey.OP_READ).attach(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle() {
        final String requestBody = handleRequest();
        logger.info("RequestBody = {}", requestBody);
        handleResponse(requestBody);
    }

    /**
     * 15:01:19.648 [pool-1-thread-1] INFO com.poc.reactorpattern.eventhandler.TcpEventHandler -- RequestBody = GET / HTTP/1.1
     * Host: localhost:8080
     * Connection: Keep-Alive
     * User-Agent: Apache-HttpClient/4.5.13 (Java/17.0.4)
     * Accept-Encoding: gzip,deflate
     * @return
     */
    private String handleRequest() {
        try {
            final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
            socketChannel.read(requestByteBuffer);
            requestByteBuffer.flip();
            return StandardCharsets.UTF_8.decode(requestByteBuffer).toString().trim();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void handleResponse(final String requestBody) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);
                final String content = "Received : " + requestBody;
                final ByteBuffer responseByteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(responseByteBuffer);
                socketChannel.close();
                logger.info(content);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, EXECUTOR_SERVICE);
    }
}
