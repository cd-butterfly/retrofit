package com.bty.retrofit.demo;

import com.bty.annotation.Service;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by duo.chen on 2018/6/14
 */
@Service
public interface NoNameTestApi {

    @GET("http://t.weather.sojson.com/api/weather/city/{cityCode}")
    Call<JsonBeanResponse> weather(@Path("cityCode") String code);
}
