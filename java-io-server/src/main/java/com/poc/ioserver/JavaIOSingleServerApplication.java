package com.poc.ioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class JavaIOSingleServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaIOSingleServerApplication.class);

    public static void main(String[] args) {

        // Create Server Socket
        try (final ServerSocket serverSocket = new ServerSocket()) {
            // Bind HostName And Port
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            // Await Client Socket Connect And Create Client Socket
            final Socket clientSocket = serverSocket.accept();

            // Create Client Socket InputStream
            final InputStream inputStream = clientSocket.getInputStream();

            // Create 1KB Input Buffer
            final byte[] inputDataBuffer = new byte[1024];
            inputStream.read(inputDataBuffer);

            // Logging Input Data
            logger.info("request: {}", new String(inputDataBuffer).trim());

            // Create Client Socket OutputStream
            final OutputStream outputStream = clientSocket.getOutputStream();

            // Create Output Buffer
            final byte[] outputData = "This is Server".getBytes(StandardCharsets.US_ASCII);
            // Write And Flush OutputData
            outputStream.write(outputData);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
