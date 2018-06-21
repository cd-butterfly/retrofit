package com.bty.retrofit.demo;

import com.bty.annotation.Service;
import com.bty.retrofit.demo.bean.CityBean;
import com.bty.retrofit.net.bean.FileDownloadRequest;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import com.bty.retrofit.net.platform.DoNetPlatform;

import java.io.File;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by duo.chen on 2018/6/12
 */
@Service(alias = "TestApiName",baseUrl = "htpp://www.baidu.com")
public interface TestApi {

    /**
     */
    @DoNetPlatform
    @POST("api/common/getdataversion")
    Call<JsonBeanResponse> getCommonDataVersion(@Body CityBean bean);


    @POST
    Call<File> download(@Url String url, @Body FileDownloadRequest fileDownloadRequest);
}
