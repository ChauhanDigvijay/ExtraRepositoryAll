package com.identity.arx.imagemanupulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeleteImage {
    public static void delete(File f) throws IOException {
        if (f.isFile()) {
            f.delete();
            for (File c : f.listFiles()) {
                delete(c);
            }
        } else if (f.getAbsolutePath().endsWith("FIR") && !f.delete()) {
            FileNotFoundException fileNotFoundException = new FileNotFoundException("Failed to delete file: " + f);
        }
    }
}
