package com.bty.retrofit.net.convert.fileConvert;

import com.bty.retrofit.net.bean.FileDownloadRequest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by duo.chen on 2017/4/21.
 */

public class FileDownloadConverterFactory extends Converter.Factory {


    public static FileDownloadConverterFactory create() {
        return new FileDownloadConverterFactory();
    }

    private FileDownloadConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {

        if (File.class.isAssignableFrom((Class<?>) type)) {
            return new FileDownloadResponseBodyConverter<File>();
        } else {
            return null;
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (FileDownloadRequest.class.isAssignableFrom((Class<?>) type)) {
            return new FileDownloadRequestBodyConverter<FileDownloadRequest>();
        } else {
            return null;
        }
    }

}

