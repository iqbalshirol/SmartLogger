package com.hackathon.smartlogger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;

/**
 * Created by ESTBLR\iqbal.shirol on 1/12/15.
 */
public class IOUtils {

    private static File getLastScreenshotTaken() {
        File pix = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File screenshots = new File(pix, "Screenshots");
        File lastModifiedFile = lastFileModified(screenshots.getAbsolutePath());
        Log.i("Test", "==========last modified file======" + lastModifiedFile.getAbsolutePath());
        return lastModifiedFile;
    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        if (files == null)
            return null;
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }

    public static String getLastCapturedScreenshot() {
        File screenshotFile = getLastScreenshotTaken();
        if (screenshotFile == null)
            return null;
        Bitmap bm = BitmapFactory.decodeFile(screenshotFile.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
