package com.bty.retrofit.net.convert.fileConvert;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bty.retrofit.net.body.ProgressResponseBody;
import com.bty.retrofit.net.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class FileDownloadResponseBodyConverter<T> implements Converter<ResponseBody, File> {

    FileDownloadResponseBodyConverter() {

    }

    @Override
    public File convert(ResponseBody value) throws IOException {

        /* value.getClass() retrofit2.OkHttpCall$ExceptionCatchingRequestBody
         * see ExceptionCatchingRequestBody
         **/

        File responseFile = null;

        try {
            Class aClass = value.getClass();
            Field field;
            field = aClass.getDeclaredField("delegate");

            field.setAccessible(true);
            ResponseBody body;
                body = (ResponseBody) field.get(value);
            if(body instanceof ProgressResponseBody){
                ProgressResponseBody prBody = ((ProgressResponseBody)body);

                String saveFilePath = String.valueOf(prBody.getParam("savePath"));

                String requestFileName = String.valueOf(prBody.getParam("fileName"));

                if(TextUtils.isEmpty(requestFileName)){
                    requestFileName = System.currentTimeMillis()+".tmp";
                }

                //If the save path is a folder, then add the request file name
                if(!TextUtils.isEmpty(saveFilePath)){
                    if (saveFilePath.endsWith(File.separator)) {
                        saveFilePath = saveFilePath + requestFileName;
                    } else {
                        saveFilePath = saveFilePath + File.separator + requestFileName;
                    }
                } else {
                    //If the save path is null, the default setting is saved to the sdcard root directory
                    saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+requestFileName;
                }

                Log.i("FileDownload","FileDownloadResponseBodyConverter " + saveFilePath);
                responseFile =  FileUtils.writeResponseBodyToDisk(prBody,saveFilePath);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return responseFile;

    }
}
