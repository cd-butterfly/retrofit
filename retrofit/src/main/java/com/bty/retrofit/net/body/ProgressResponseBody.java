package com.bty.retrofit.net.body;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by duo.chen on 2017/4/21.
 */

public class ProgressResponseBody extends ResponseBody {

    private  ResponseBody responseBody;
    private  ProgressResponseListener progressListener;

    private Map<String,Object> params = new HashMap<>();
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            //Current readed bytes
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                //Increase the number of bytes currently read, if the read is complete bytesRead will return -1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //If contentLength() does not know the length, it returns -1
                if (null != progressListener) {
                    progressListener.onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }

    public void setProgressListener(ProgressResponseListener progressListener) {
        this.progressListener = progressListener;
    }

    public interface ProgressResponseListener {
        void onResponseProgress(long bytesRead, long contentLength, boolean done);
    }

    public void putParams(String key, Object value) {
        params.put(key,value);
    }

    public void putAll(Map map) {
        params.putAll(map);
    }

    public Object getParam(String key) {
        if (!TextUtils.isEmpty(key)) {
            return params.get(key);
        } else
            return null;
    }

}
