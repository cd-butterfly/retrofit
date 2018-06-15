package com.bty.retrofit.demo;

import android.util.Log;

import com.bty.retrofit.Api;
import com.bty.retrofit.demo.bean.CityBean;
import com.bty.retrofit.net.bean.FileDownloadRequest;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import com.bty.retrofit.net.body.ProgressResponseBody;
import com.bty.retrofit.net.callAdapter.LifeCallAdapterFactory;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by duo.chen on 2018/6/14
 */
public class MainActivityPresenter{

    private MainActivity mainActivity;

    MainActivityPresenter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    void request(){
        Api.getTestApiName().getCommonDataVersion(new CityBean()).enqueue(new LifeCallAdapterFactory.LifeCallback<JsonBeanResponse>(mainActivity) {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("Retrofit",response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    void download() {
        String url = "http://cdn.llsapp.com/yy/image/3b0430db-5ff4-455c-9c8d-0213eea7b6c4.jpg";

        FileDownloadRequest fileDownloadRequest = new FileDownloadRequest();
        fileDownloadRequest.setFileName("pic.jpg");
        fileDownloadRequest.setSavPath(mainActivity.getExternalCacheDir().getPath());
        fileDownloadRequest.setProgressResponseListener(new ProgressResponseBody.ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.i("Retrofit","bytesRead "  + bytesRead + " contentLength " + contentLength);
            }
        });
        Api.getTestApiName().download(url,fileDownloadRequest).enqueue(new LifeCallAdapterFactory.LifeCallback<JsonBeanResponse>(mainActivity) {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });



    }

}
