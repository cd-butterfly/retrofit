package com.bty.retrofit.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bty.retrofit.demo.bean.PostBean;
import com.bty.retrofit.net.bean.FileDownloadRequest;
import com.bty.retrofit.net.bean.FormFile;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import com.bty.retrofit.net.body.ProgressResponseBody;
import com.bty.retrofit.net.callAdapter.LifeCallAdapterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
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

    }

    void testGet(){
        Api.getTestApi().getweather("101020100").enqueue(new Callback<JsonBeanResponse>() {
            @Override
            public void onResponse(Call<JsonBeanResponse> call, Response<JsonBeanResponse> response) {

            }

            @Override
            public void onFailure(Call<JsonBeanResponse> call, Throwable t) {

            }
        });
    }

    void testPost(){
        Api.getTestApi().testPost(new PostBean()).enqueue(new Callback<JsonBeanResponse>() {
            @Override
            public void onResponse(Call<JsonBeanResponse> call, Response<JsonBeanResponse> response) {

            }

            @Override
            public void onFailure(Call<JsonBeanResponse> call, Throwable t) {

            }
        });
    }

    void testdownload() {
        String url = "https://repo1.maven.org/maven2/com/squareup/okhttp3/mockwebserver/3.12.1/mockwebserver-3.12.1.jar";

        FileDownloadRequest fileDownloadRequest = new FileDownloadRequest();
        fileDownloadRequest.setFileName("pic.jpg");
        fileDownloadRequest.setSavPath(mainActivity.getExternalCacheDir().getPath());
        fileDownloadRequest.setProgressResponseListener(new ProgressResponseBody.ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.i("Retrofit","bytesRead "  + bytesRead + " contentLength " + contentLength);
            }
        });
        Api.getTestApi().download(url,fileDownloadRequest).enqueue(new LifeCallAdapterFactory.LifeCallback<File>(mainActivity) {
            @Override
            public void onResponse(Call<File> call, Response<File> response) {

            }

            @Override
            public void onFailure(Call<File> call, Throwable t) {

            }
        });
    }

    void testUpLoad(){
        //sample for compress image
        Bitmap bmp = BitmapFactory.decodeFile("");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        FormFile formFile = new FormFile("",bos.toByteArray(),"","");
    }

}
