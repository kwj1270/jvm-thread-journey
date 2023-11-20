package com.poc.lab.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JavaScatterGatherApplication {
    private static String FILENAME = "java-io-test/src/main/resources/files/January18.txt";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        gatherAndWriteToFile("Welcome To My Domain", "Have a Nice Day");
        scatter();
    }

    private static void scatter() {
        final ByteBuffer lengthBuffer1 = ByteBuffer.allocate(Integer.BYTES);
        final ByteBuffer lengthBuffer2 = ByteBuffer.allocate(Integer.BYTES);

        try (final FileInputStream fileInputStream = new FileInputStream(FILENAME);
             final ScatteringByteChannel scatterChannel = fileInputStream.getChannel()
        ) {
            scatterChannel.read(new ByteBuffer[]{lengthBuffer1, lengthBuffer2});
            lengthBuffer1.position(0);
            lengthBuffer2.position(0);

            final int length1 = lengthBuffer1.asIntBuffer().get();
            final int length2 = lengthBuffer2.asIntBuffer().get();

            System.out.println("Getting the length of variable 1: " + length1);
            System.out.println("Getting the length of variable 2: " + length2);

            final ByteBuffer dataBuffer1 = ByteBuffer.allocate(length1);
            final ByteBuffer dataBuffer2 = ByteBuffer.allocate(length2);

            scatterChannel.read(new ByteBuffer[]{dataBuffer1, dataBuffer2});

            System.out.println(new String(dataBuffer1.array(), CHARSET));
            System.out.println(new String(dataBuffer2.array(), CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gatherAndWriteToFile(String input1, String input2) {
        final ByteBuffer lengthBuffer1 = ByteBuffer.allocate(Integer.BYTES);
        final ByteBuffer lengthBuffer2 = ByteBuffer.allocate(Integer.BYTES);
        final ByteBuffer dataBuffer1 = ByteBuffer.wrap(input1.getBytes());
        final ByteBuffer dataBuffer2 = ByteBuffer.wrap(input2.getBytes());

        final int length1 = input1.length();
        final int length2 = input2.length();

        lengthBuffer1.asIntBuffer().put(length1);
        lengthBuffer2.asIntBuffer().put(length2);

        System.out.println("Length of the Gathering1= " + length1);
        System.out.println("Length of the Gathering2= " + length2);

        try (final FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
             final GatheringByteChannel gatherChannel = fileOutputStream.getChannel()
        ) {
            gatherChannel.write(new ByteBuffer[]{lengthBuffer1, lengthBuffer2, dataBuffer1, dataBuffer2});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}