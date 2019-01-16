package com.bty.retrofit.demo.interceptor;

import com.bty.retrofit.demo.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by duo.chen
 */

public class HeaderInterceptor implements Interceptor {

    private final static String APP_VER = "App-Ver";
    private final static String APP_PLATFORM = "App-Platform";
    private final static String DEVICE_ID = "Device-Id";
    private final static String UID = "U-Id";
    private final static String TOKEN = "token";
    private static final String PLATFORM = "Android";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader(APP_VER, BuildConfig.VERSION_NAME)
                .addHeader(APP_PLATFORM, PLATFORM)
                .addHeader(DEVICE_ID, "123344")
                .addHeader(UID, "1222333")
                .addHeader(TOKEN, "11111111111111111")
                .addHeader("name", "android_demo")
                .addHeader("code", String.valueOf(BuildConfig.VERSION_CODE));
        return chain.proceed(builder.build());
    }
}
