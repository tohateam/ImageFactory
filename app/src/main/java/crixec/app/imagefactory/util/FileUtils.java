package crixec.app.imagefactory.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import crixec.app.imagefactory.core.ExceptionHandler;

public class FileUtils {

    public FileUtils() {
        // TODO Auto-generated constructor stub
    }

    public static StringBuilder readInputStream(InputStream is) {
        StringBuilder str = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            if (line != null) {
                str.append(line);
            }
            while ((line = br.readLine()) != null) {
                str.append("\n" + line);
            }
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return str;
    }

    public static int writeFile(InputStream is, File file) throws IOException {
        int code = -1;
        OutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[is.available()];
        while ((code = is.read(buf)) != -1) {
            fos.write(buf);
            fos.flush();
        }
        fos.close();
        return code;
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static void setValid(File bin) {
        bin.setReadable(true, false);
        bin.setWritable(true, false);
        bin.setExecutable(true, false);
    }

    public static void copyFile(File from, File to) throws IOException {
        Log.i("FileUtils", "copy file from=" + from.getPath() + " to=" + to.getPath());
        InputStream fis = new FileInputStream(from);
        writeFile(fis, to);
        fis.close();
    }
}
