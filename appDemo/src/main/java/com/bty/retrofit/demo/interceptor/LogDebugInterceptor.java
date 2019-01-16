package com.bty.retrofit.demo.interceptor;

import android.annotation.SuppressLint;
import com.bty.retrofit.demo.log.FloatingWindow.LogManager;
import com.bty.retrofit.demo.log.FloatingWindow.OkHttpRequestLog;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static okhttp3.internal.platform.Platform.INFO;

public class LogDebugInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte requestBody)
         *
         * <-- 200 OK (22ms, 6-byte requestBody)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public LogDebugInterceptor() {
        this(Logger.DEFAULT);
    }

    public LogDebugInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.BODY;

    /**
     * Change the level at which this interceptor logs.
     */
    public LogDebugInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        OkHttpRequestLog okHttpRequestLog = new OkHttpRequestLog();

        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        okHttpRequestLog.url = request.url().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        okHttpRequestLog.timeStamp = String.valueOf(simpleDateFormat.format(new Date()));
        okHttpRequestLog.method = request.method();
        okHttpRequestLog.requestHeaders = request.headers().toString();

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte requestBody)";
        }
        logger.log(requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request requestBody headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.log("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request requestBody as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded requestBody omitted)");
            } else {
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
                    logger.log("");
                    if (isPlaintext(buffer)) {
                        logger.log(msg);
                        logger.log("--> END " + request.method()
                                + " (" + requestBody.contentLength() + "-byte requestBody)");
                    } else {
                        logger.log("--> END " + request.method() + " (binary "
                                + requestBody.contentLength() + "-byte requestBody omitted)");
                    }
                    okHttpRequestLog.requestBody = bodyToString(msg);
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " requestBody" : "") + ')');

        okHttpRequestLog.statusCode = String.valueOf(response.code());

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded requestBody omitted)");
            } else {
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

                    if (!isPlaintext(cloneBuffer)) {
                        logger.log("");
                        logger.log("<-- END HTTP (binary " + cloneBuffer.size() + "-byte requestBody omitted)");
                        return response;
                    }

                    if (contentLength != 0) {
                        logger.log("");
                        logger.log(msg);
                    }

                    logger.log("<-- END HTTP (" + cloneBuffer.size() + "-byte requestBody)");
                    okHttpRequestLog.responseBody = bodyToString(msg);
                }
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
        } catch (JSONException e) {
            logBodyInfo = "msg is not json";
        }
        return logBodyInfo;
    }

    /**
     * Returns true if the requestBody in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
