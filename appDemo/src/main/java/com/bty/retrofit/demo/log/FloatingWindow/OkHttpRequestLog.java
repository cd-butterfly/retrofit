package com.bty.retrofit.demo.log.FloatingWindow;

import java.io.Serializable;

public class OkHttpRequestLog implements Serializable {
    public String url;
    public String timeStamp;
    public String requestBody;
    public String responseBody;
    public String method;
    public String requestHeaders;
    public String statusCode;
}
