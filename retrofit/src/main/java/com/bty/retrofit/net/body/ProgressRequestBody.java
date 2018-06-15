package com.bty.retrofit.net.body;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by duo.chen on 2017/4/24.
 */

public class ProgressRequestBody extends RequestBody {

    private Object tag;

    private Map<String,String> paramMap;

    /**
     * hack method,Body only supports post mode. Most of the downloads need to hack into a get request
     */
    private String method = "GET";

    private RequestBody requestBody;

    public ProgressRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        paramMap = new HashMap<>();
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        requestBody.writeTo(sink);
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void putParam(String key, String value) {
        paramMap.put(key,value);
    }

    public String getParam(String key) {
        return paramMap.get(key);
    }

    public Map map() {
        return paramMap;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
