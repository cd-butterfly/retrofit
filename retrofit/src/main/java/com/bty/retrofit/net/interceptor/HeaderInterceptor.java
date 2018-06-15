package com.bty.retrofit.net.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by duo.chen on 2017/4/21.
 */

public class HeaderInterceptor implements Interceptor {

    private HashMap<String, String> headers = new HashMap<>();

    public HeaderInterceptor(HashMap<String,String> headers) {
        this.headers.putAll(headers);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();

        for (String key :  headers.keySet()) {
            builder.addHeader(key,headers.get(key));
        }

        Request request = builder
                .build();

        return chain.proceed(request);
    }
}
