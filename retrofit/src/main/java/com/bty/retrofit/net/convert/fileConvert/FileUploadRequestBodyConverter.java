package com.bty.retrofit.net.convert.fileConvert;

import android.support.annotation.NonNull;

import com.bty.retrofit.net.bean.FileUploadRequest;
import com.bty.retrofit.net.bean.FormFile;
import com.bty.retrofit.net.serurity.Sign;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by duo.chen on 2017/4/5
 */

public class FileUploadRequestBodyConverter implements Converter<FileUploadRequest, RequestBody> {

    private Sign.Factory factory;

    public FileUploadRequestBodyConverter(Sign.Factory factory) {
        this.factory = factory;
    }

    @Override
    public RequestBody convert(@NonNull FileUploadRequest request) throws IOException {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        Map<String,Object> requestMap = new HashMap<>();

        for (Class<?> clazz = request.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().contains("files")) {
                    try {
                        if (null != field.get(request)) {
                            requestMap.put(field.getName(), field.get(request));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (factory.get() != null) {
            Sign sign = factory.get();
            Map<String, Object> signMap = sign.sign(requestMap, sign.getSignKey());
            if (signMap != null) {
                requestMap = signMap;
            }
        }

        for (Map.Entry entry : requestMap.entrySet()) {
            if (null != entry.getValue()) {
                builder.addPart(MultipartBody.Part.createFormData(String.valueOf(entry.getKey()),
                        String.valueOf(entry.getValue())));
            }
        }

        for (FormFile file:request.getFiles()){
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("application/octet-stream"), file.getFile());

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("File", file.getFile().getName(), requestFile);
            builder.addPart(body);
        }

        return builder.build();
    }
}
