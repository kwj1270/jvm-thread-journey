
package com.poc.aioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

public class JavaAIOFutureServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaAIOFutureServerApplication.class);

    public static void main(String[] args) {

        // Create Server Socket
        try (final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            // Bind HostName And Port
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            final Future<AsynchronousSocketChannel> clientSocketFuture = serverSocketChannel.accept();
            while (!clientSocketFuture.isDone()) {
                Thread.sleep(100);
                logger.info("Waiting...");
            }

            final AsynchronousSocketChannel clientSocketChannel = clientSocketFuture.get();
            final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
            final Future<Integer> channelRead = clientSocketChannel.read(requestByteBuffer);
            while (!channelRead.isDone()) {
                logger.info("Reading...");
            }

            requestByteBuffer.flip();
            final String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
            logger.info("request: {}", requestBody.trim());

            final ByteBuffer responseByteBuffer = ByteBuffer.wrap("This is Server".getBytes(StandardCharsets.UTF_8));
            clientSocketChannel.write(responseByteBuffer);
            clientSocketChannel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
