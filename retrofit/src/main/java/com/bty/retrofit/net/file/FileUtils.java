package com.bty.retrofit.net.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by duo.chen on 2018/6/11
 */
public class FileUtils {

    /**
     * write response to file
     * @param body {@link okhttp3.ResponseBody}
     * @param path file save path
     * @return file saved
     */
    public static File writeResponseBodyToDisk(ResponseBody body, String path) {

        File saveFile = null;
        try {

            saveFile = new File(path);

            createDirs(saveFile);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(saveFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                }

                outputStream.flush();

                return saveFile;
            } catch (IOException e) {
                return saveFile;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return saveFile;
        }
    }

    private static boolean createDirs(File file){
        if(file != null){
            String dir = file.getParent();
            return createDirs(dir);
        } else {
            return false;
        }
    }

    private static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        return !file.exists() && file.mkdirs();
    }
}
