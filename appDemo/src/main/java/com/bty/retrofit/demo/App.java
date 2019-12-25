package com.bty.retrofit.demo;

import android.app.Application;

import com.bty.retrofit.demo.interceptor.HeaderInterceptor;
import com.bty.retrofit.demo.interceptor.LogDebugInterceptor;
import com.bty.retrofit.net.RetrofitManager;
import com.facebook.stetho.Stetho;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by duo.chen on 2018/6/14
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        RetrofitManager.Config.Builder builder = new RetrofitManager.Config.Builder();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pt", "android");
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderInterceptor());
        interceptors.add(new LogDebugInterceptor());
        builder.setInterceptors(interceptors);
        builder.setProxy(Proxy.NO_PROXY);

        builder.setBaseUrl(BuildConfig.BASE_URL)
                .setLog(true)
                .setHeaders(hashMap);

        RetrofitManager.init(builder.build());
    }
}
