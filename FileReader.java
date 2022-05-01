package com.company.tests.filehandler;

import java.io.*;

public class FileReader {
    private final FileInputStream inputStream;
    private final ByteArrayOutputStream bos;
    private final File file;

    public FileReader(String path) throws FileNotFoundException {
        file = new File(path);
        inputStream = new FileInputStream(file);
        bos = new ByteArrayOutputStream();
    }

    public byte[] read(long size) throws IOException {
        bos.flush();
        size = (size <= 0) ? file.length() : size;
        byte[] buffer = new byte[(int)size];
        int total = 0;
        int read;
        while (total < size){
            read = inputStream.read(buffer, 0, buffer.length);
            if (read > 0){
                total += read;
                bos.write(buffer, 0, read);
            }
        }
        return bos.toByteArray();
    }

    public void close() throws IOException {
        inputStream.close();
        bos.flush();
    }
}
