package com.bty.retrofit.net.convert.fileConvert;

import com.bty.retrofit.net.bean.FileDownloadRequest;
import com.bty.retrofit.net.body.ProgressRequestBody;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by duo.chen on 2017/4/5
 */

public class  FileDownloadRequestBodyConverter<T> implements Converter<FileDownloadRequest, RequestBody> {
    @Override
    public RequestBody convert(FileDownloadRequest request) throws IOException {
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(RequestBody.create(
                MediaType.parse("text/plain; charset=utf-8"), ""));
        if (null != request.getProgressResponseListener()) {
            progressRequestBody.setTag(request.getProgressResponseListener());
        }

        progressRequestBody.putParam("savePath",request.getSavPath());
        progressRequestBody.putParam("fileName",request.getFileName());
        return progressRequestBody;
    }
}
