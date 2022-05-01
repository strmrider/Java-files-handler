package com.company.tests.filehandler;

import java.io.*;

public class FileWriter {
    private final FileOutputStream outputStream;

    public FileWriter(String path) throws FileNotFoundException {
        File file = new File(path);
        outputStream = new FileOutputStream(file);
    }

    public void write(byte[] data) throws IOException {
        outputStream.write(data);
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
