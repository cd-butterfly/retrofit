package com.bty.retrofit.demo;

import com.bty.annotation.Service;
import com.bty.retrofit.demo.bean.PostBean;
import com.bty.retrofit.net.bean.FileDownloadRequest;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.io.File;

/**
 * Created by duo.chen on 2018/6/12
 */
@Service(alias = "TestApi",baseUrl = "http://t.weather.sojson.com/")
public interface TestApi {

    @GET("/api/weather/city/{cityCode}")
    Call<JsonBeanResponse> getweather(@Path("cityCode") String code);


    @POST("http://10.55.5.34:8081/api/auth/token/issue")
    Call<JsonBeanResponse> testPost(@Body PostBean postBean);

    @POST
    Call<File> download(@Url String url, @Body FileDownloadRequest fileDownloadRequest);
}
