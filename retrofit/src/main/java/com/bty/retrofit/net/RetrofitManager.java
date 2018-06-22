package com.bty.retrofit.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bty.retrofit.net.callAdapter.LifeCallAdapterFactory;
import com.bty.retrofit.net.convert.fileConvert.FileDownloadConverterFactory;
import com.bty.retrofit.net.convert.fileConvert.FileUploadConverterFactory;
import com.bty.retrofit.net.convert.jsonConvert.FastJsonConverterFactory;
import com.bty.retrofit.net.interceptor.FileDownloadInterceptor;
import com.bty.retrofit.net.interceptor.HeaderInterceptor;
import com.bty.retrofit.net.serurity.Sign;
import com.bty.retrofit.net.serurity.sign.DefaultSignFactory;
import com.bty.retrofit.net.ssl.TrustAllCerts;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by duo.chen on 2017/3/28
 */

public class RetrofitManager {

    private static Config config;

    private static HashMap<String, Object> apiCacheMap = new HashMap<>();
    private static CookieJar cookieJar = new CookieJar() {

        HashMap<String, Cookie> cookieHashMap = new HashMap<>();

        @Override
        public void saveFromResponse(@NonNull HttpUrl url, List<Cookie> cookies) {
            if (cookies.size() > 0) {
                for (Cookie cookie : cookies) {
                    //Any existing cookie with the same name will be replaced with the new cookie
                    cookieHashMap.put(cookie.name(), cookie);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
            return new ArrayList<>(cookieHashMap.values());
        }
    };

    private RetrofitManager() {

    }

    public static void init(Config config) {
        if (RetrofitManager.config == null) {
            RetrofitManager.config = config;
        } else {
            throw new SecurityException("init can not be called twice");
        }
    }

    public static <T> T create(Class<T> service) {
        if (!CacheMap().containsKey(service.getName())) {
            T instance = retrofit().create(service);
            CacheMap().put(service.getName(), instance);
        }
        return (T) CacheMap().get(service.getName());
    }

    public static <T> T create(Class<T> service, String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            return create(service);
        } else {
            String key = service.getName() + baseUrl;
            if (CacheMap().containsKey(key)) {
                T instance = retrofit(baseUrl).create(service);
                CacheMap().put(key, instance);
            }
            return (T) CacheMap().get(key);
        }
    }

    private static HashMap<String, Object> CacheMap() {
        return apiCacheMap;
    }

    private static Retrofit retrofit() {
        return retrofit(null);
    }

    private static Retrofit retrofit(String url) {

        if (config == null) {
            throw new NullPointerException("no config");
        }

        //Retrofit choose Converter,Depending on the type of object that needs to be converted,
        // when adding a converter, notice the inclusion of the type supported by the Converter and its order.

        Sign.Factory factory = config.factory == null ? DefaultSignFactory.create("") : config.factory;

        Retrofit.Builder builder = new Retrofit.Builder();

        if (config.converterFactory != null) {
            builder.addConverterFactory(config.converterFactory);
        }

        builder.addConverterFactory(FastJsonConverterFactory.create(factory))
                .addConverterFactory(FileUploadConverterFactory.create(factory))
                .addConverterFactory(FileDownloadConverterFactory.create())
                .addCallAdapterFactory(LifeCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(getClient(config.isLog()));

        if (TextUtils.isEmpty(url)) {
            return builder.baseUrl(config.baseUrl).build();
        } else {
            return builder.baseUrl(url).build();
        }
    }

    private static OkHttpClient getClient(boolean log) {

        /**Choosing between application and network interceptors
         * https://github.com/square/okhttp/wiki/Interceptors
         *
         * addNetworkInterceptor(loggingInterceptor)
         */
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (!log) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        X509TrustManager x509TrustManager = config.certs == null ? new TrustAllCerts() : config.certs;
        SSLSocketFactory sslSocketFactory = config.sslSocketFactory == null ? createSSLSocketFactory() : config.sslSocketFactory;

        //config headers,timeout,interceptors
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (config.interceptor != null) {
            builder.addInterceptor(config.interceptor);
        }

        builder.addInterceptor(new HeaderInterceptor(config.headers))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new FileDownloadInterceptor())
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .cookieJar(cookieJar);

        if (log) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            //Ignore
        }

        return ssfFactory;
    }

    public static Config getConfig() {
        return config;
    }

    public static class Config {

        private boolean log;
        private String baseUrl;
        private Sign.Factory factory;
        private X509TrustManager certs;
        private SSLSocketFactory sslSocketFactory;
        private HashMap<String, String> headers = new HashMap<>();
        private Interceptor interceptor;
        private Converter.Factory converterFactory;

        Config(Builder builder) {
            this.log = builder.log;
            this.baseUrl = builder.baseUrl;
            this.factory = builder.signFactory;
            this.certs = builder.certs;
            this.sslSocketFactory = builder.sslSocketFactory;
            this.headers = builder.headers;
            this.interceptor = builder.interceptor;
            this.converterFactory = builder.converterFactory;
        }

        public boolean isLog() {
            return log;
        }

        public void setLog(boolean log) {
            this.log = log;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

        public Sign.Factory getFactory() {
            return factory;
        }

        public void setFactory(Sign.Factory factory) {
            this.factory = factory;
        }

        public X509TrustManager getCerts() {
            return certs;
        }

        public void setCerts(X509TrustManager certs) {
            this.certs = certs;
        }

        public static final class Builder {

            private boolean log;
            private String baseUrl;
            private Sign.Factory signFactory;
            private X509TrustManager certs;
            private SSLSocketFactory sslSocketFactory;
            private HashMap<String, String> headers = new HashMap<>();
            private Interceptor interceptor;
            private Converter.Factory converterFactory;

            public Builder setLog(boolean log) {
                this.log = log;
                return this;
            }

            public Builder setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
                return this;
            }

            public Builder setSignFactory(Sign.Factory signFactory) {
                this.signFactory = signFactory;
                return this;
            }

            public Builder setCerts(X509TrustManager certs) {
                this.certs = certs;
                return this;
            }

            public Builder setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
                this.sslSocketFactory = sslSocketFactory;
                return this;
            }

            public Builder setHeaders(HashMap<String, String> headers) {
                this.headers = headers;
                return this;
            }

            public Builder setInterceptor(Interceptor interceptor) {
                this.interceptor = interceptor;
                return this;
            }

            public Builder setConverterFactory(Converter.Factory converterFactory) {
                this.converterFactory = converterFactory;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }

}
