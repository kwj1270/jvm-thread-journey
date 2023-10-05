package com.poc.nioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class A_JavaNIOBlockingServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(A_JavaNIOBlockingServerApplication.class);

    public static void main(String[] args) {

        // Create Server Socket
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // Bind HostName And Port
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

            while (true) {
                final SocketChannel clientSocket = serverSocketChannel.accept();
                final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
                clientSocket.read(requestByteBuffer);
                requestByteBuffer.flip();
                final String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
                logger.info("request: {}", requestBody.trim());

                final ByteBuffer responseByteBuffer = ByteBuffer.wrap("This is Server".getBytes(StandardCharsets.UTF_8));
                clientSocket.write(responseByteBuffer);
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
