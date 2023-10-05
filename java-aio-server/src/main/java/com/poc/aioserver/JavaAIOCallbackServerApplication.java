
package com.poc.aioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaAIOCallbackServerApplication {

    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);
    private static final Logger logger = LoggerFactory.getLogger(JavaAIOCallbackServerApplication.class);

    public static void main(String[] args) throws InterruptedException {

        // Create Server Socket
        try (final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            // Bind HostName And Port
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            serverSocketChannel.accept(null, new CompletionHandler<>() {
                @Override
                public void completed(final AsynchronousSocketChannel clientSocket, final Object attachment) {
                    final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
                    clientSocket.read(requestByteBuffer, null, new CompletionHandler<>() {
                        @Override
                        public void completed(final Integer result, final Object attachment) {
                            try {
                                requestByteBuffer.flip();
                                final String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
                                logger.info("request: {}", requestBody.trim());

                                final ByteBuffer responseByteBuffer = ByteBuffer.wrap("This is Server".getBytes(StandardCharsets.UTF_8));
                                clientSocket.write(responseByteBuffer);
                                clientSocket.close();
                                final int taskIdentifiedId = atomicInteger.getAndIncrement();
                                logger.info("Run handleRequest Task Number = {}", taskIdentifiedId);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void failed(final Throwable exc, final Object attachment) {
                        }
                    });
                }

                @Override
                public void failed(final Throwable exc, final Object attachment) {
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
