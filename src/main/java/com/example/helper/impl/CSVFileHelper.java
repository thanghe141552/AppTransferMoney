package com.example.helper.impl;

import com.example.constant.AppConstant;
import com.example.helper.EntityParseHelper;
import com.example.helper.FileHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVFileHelper<T> implements FileHelper<T> {

    @Override
    public void writeToFile(String fileName, List<T> content) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw")) {
            randomAccessFile.seek(randomAccessFile.length());
            FileChannel channel = randomAccessFile.getChannel();
            for (T item : content) {
                String writeData = item.toString() + "\n";
                ByteBuffer buffer = ByteBuffer.wrap(writeData.getBytes());
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> readFromFile(String fileName, EntityParseHelper<T> parseHelper) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
             FileChannel channel = randomAccessFile.getChannel()) {

            List<T> listObject = new ArrayList<>();
            ByteBuffer buffer = ByteBuffer.allocate(AppConstant.BUFFER_SIZE);
            while (channel.read(buffer) > 0) {
                buffer.flip();

                while (buffer.hasRemaining()){
                    String line = readLine(buffer);
                    listObject.add(parseHelper.parse(line));
                }

                buffer.clear();
            }

            return listObject;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readLine(ByteBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder();
//        Charset charset = StandardCharsets.UTF_8;
        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();
            if (currentByte == '\n'){
                break;
            }
            stringBuilder.append((char) currentByte);
        }
        return stringBuilder.toString();
    }
}
