package com.womensafety.httpimage;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemPersistence implements BitmapCache {
    private static boolean DEBUG = true;
    private static String TAG = "FileSystemPersistence";
    private String mBaseDir;

    public FileSystemPersistence(String baseDir) {
        this.mBaseDir = baseDir;
    }

    public void clear() {
        try {
            removeDir(new File(this.mBaseDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(String key) {
        return new File(new File(this.mBaseDir), key).exists();
    }

    public void invalidate(String key) {
    }

    public Bitmap loadData(String key) {
        if (exists(key)) {
            return BitmapUtil.decodeFile(new File(new File(this.mBaseDir), key).getAbsolutePath(), HttpImageManager.DECODING_MAX_PIXELS_DEFAULT);
        }
        return null;
    }

    public void storeData(String key, Object data) {
        Throwable th;
        File file = new File(new File(this.mBaseDir), key);
        FileOutputStream outputStream = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream outputStream2 = new FileOutputStream(file);
            try {
                outputStream2.write((byte[]) data);
                outputStream2.flush();
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                        outputStream = outputStream2;
                        return;
                    } catch (IOException e) {
                        outputStream = outputStream2;
                        return;
                    }
                }
            } catch (IOException e2) {
                outputStream = outputStream2;
                try {
                    if (DEBUG) {
                        Log.e(TAG, "error storing bitmap");
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e4) {
                        }
                    }

                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = outputStream2;
                if (outputStream != null) {
                    outputStream.close();
                }

            }
        } catch (IOException e5) {
            if (DEBUG) {
                Log.e(TAG, "error storing bitmap");
            }
            if (outputStream != null) {

            }
        }
    }

    private void removeDir(File d) throws IOException {
        File candir = d.getCanonicalFile();
        if (candir.equals(d.getAbsoluteFile())) {
            File[] files = candir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete() && file.isDirectory()) {
                        removeDir(file);
                    }
                }
            }
            d.delete();
        }
    }
}
