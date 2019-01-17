package com.bty.retrofit.demo.interceptor;

import android.annotation.SuppressLint;
import com.bty.retrofit.demo.log.FloatingWindow.LogManager;
import com.bty.retrofit.demo.log.FloatingWindow.OkHttpRequestLog;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class LogDebugInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public LogDebugInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        OkHttpRequestLog okHttpRequestLog = new OkHttpRequestLog();

        Request request = chain.request();

        okHttpRequestLog.url = request.url().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        okHttpRequestLog.timeStamp = String.valueOf(simpleDateFormat.format(new Date()));
        okHttpRequestLog.method = request.method();
        okHttpRequestLog.requestHeaders = request.headers().toString();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        if (hasRequestBody && !bodyEncoded(request.headers())) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            Buffer cloneBuffer = buffer.clone();
            String msg = cloneBuffer.readString(charset);

            if (cloneBuffer.size() <= 1024 * 512) {
                okHttpRequestLog.requestBody = bodyToString(msg);
            }
        }

        long startNs = System.nanoTime();
        long tookMs;
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            okHttpRequestLog.statusCode = "exception";
            okHttpRequestLog.tookMs = String.valueOf(tookMs);
            LogManager.INSTANCE.log(okHttpRequestLog);
            throw e;
        }
        tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        okHttpRequestLog.tookMs = String.valueOf(tookMs);

        ResponseBody responseBody = response.body();
        long contentLength = 0;
        if (responseBody != null) {
            contentLength = responseBody.contentLength();
        }
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

        okHttpRequestLog.statusCode = String.valueOf(response.code());
        okHttpRequestLog.bodySize = bodySize;

        if (HttpHeaders.hasBody(response) && !bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire requestBody.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            Buffer cloneBuffer = buffer.clone();
            if (cloneBuffer.size() <= 1024 * 512) {
                String msg = cloneBuffer.readString(charset);
                okHttpRequestLog.responseBody = bodyToString(msg);
            }
        }
        LogManager.INSTANCE.log(okHttpRequestLog);
        return response;
    }

    private String bodyToString(String msg) {
        String logBodyInfo;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                logBodyInfo = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                logBodyInfo = jsonArray.toString(4);
            } else {
                logBodyInfo = new JSONObject(msg).toString(4);
            }
        } catch (Exception e) {
            logBodyInfo = "bodyToString json exception";
        }
        logBodyInfo = logBodyInfo.replace("\\/", "/");
        return logBodyInfo;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
