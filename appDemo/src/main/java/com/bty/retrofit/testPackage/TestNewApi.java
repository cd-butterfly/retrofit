package com.bty.retrofit.testPackage;

import com.bty.annotation.Service;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Service(alias = "TestNewApi",baseUrl = "http://t.weather.sojson.com/")
public interface TestNewApi {

    @GET("/api/weather/city/{cityCode}")
    Call<JsonBeanResponse> getweatherNew(@Path("cityCode") String code);
}
