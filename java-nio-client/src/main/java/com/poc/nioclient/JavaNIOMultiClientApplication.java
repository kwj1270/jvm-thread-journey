package com.poc.nioclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaNIOMultiClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaNIOMultiClientApplication.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);

    public static void main(String[] args) {
        final List<CompletableFuture> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try (final SocketChannel socketChannel = SocketChannel.open()) {
                    socketChannel.connect(new InetSocketAddress("localhost", 8080));

                    final String requestContent = "This is Socket Client";
                    final ByteBuffer requestByteBuffer = ByteBuffer.wrap(requestContent.getBytes(StandardCharsets.UTF_8));
                    socketChannel.write(requestByteBuffer);

                    final ByteBuffer response = ByteBuffer.allocateDirect(1024);
                    while (socketChannel.read(response) > 0) {
                        response.flip();
                        logger.info("response: {}", StandardCharsets.UTF_8.decode(response).toString().trim());
                        response.clear();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, EXECUTOR_SERVICE);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        EXECUTOR_SERVICE.shutdown();
        long end = System.currentTimeMillis();
        logger.info("RPS = {} ms", (end - start) / 1000.0);
    }
}
