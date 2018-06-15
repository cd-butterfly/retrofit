package com.bty.retrofit.net.interceptor;

import com.bty.retrofit.net.body.ProgressRequestBody;
import com.bty.retrofit.net.body.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by duo.chen on 2017/4/21
 */

public class FileDownloadInterceptor implements Interceptor {

    public FileDownloadInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        RequestBody requestBody = request.body();
        if (requestBody instanceof ProgressRequestBody) {

            ProgressRequestBody progressRequestBody = (ProgressRequestBody) requestBody;

            if (progressRequestBody.getMethod().equals("GET")) {
                request = request.newBuilder().get().build();
            } else {
                request = request.newBuilder().method(progressRequestBody.getMethod(), requestBody).build();
            }
            Response originResponse = chain.proceed(request);
            ProgressResponseBody progressResponseBody = new ProgressResponseBody(originResponse.body());

            if (progressRequestBody.getTag() != null) {
                progressResponseBody.setProgressListener((
                        ProgressResponseBody.ProgressResponseListener)
                        progressRequestBody.getTag());

            }
            progressResponseBody.putAll(progressRequestBody.map());


            return originResponse.newBuilder().body(progressResponseBody).build();
        } else {
            return chain.proceed(request);
        }

    }
}
