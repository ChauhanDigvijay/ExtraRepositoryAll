package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Services;

import android.content.Context;

import com.fishbowl.LoyaltyTabletApp.Utils.FileNameGenerator;

import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PassSaveService {
    Context context;

    public PassSaveService(Context context) {
        super();
        this.context = context;
    }

    public File InflatePkPassInTempDir(InputStream pkpassFile) throws IOException, ParseException {
        File cacheDir = context.getCacheDir();
        String name = new FileNameGenerator().randomName();
        File inflateHere = new File(cacheDir, name);
        inflateHere.mkdir();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(pkpassFile);
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            File newFile = new File(inflateHere, ze.getName());
            FileOutputStream stream = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                stream.write(buffer, 0, len);
            }
            stream.close();
            ze = zis.getNextEntry();
        }
        zis.close();
        return inflateHere;
    }
}
