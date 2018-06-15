package com.bty.retrofit.demo;

import android.app.Application;

import com.bty.retrofit.demo.util.SignUtil;
import com.bty.retrofit.net.RetrofitManager;
import com.bty.retrofit.net.serurity.Sign;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duo.chen on 2018/6/14
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitManager.Config.Builder builder = new RetrofitManager.Config.Builder();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pt", "android");
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        builder.setBaseUrl(BuildConfig.BASE_URL).
                setSignFactory(new Sign.Factory() {
                    @Override
                    public Sign get() {
                        return new Sign() {
                            @Override
                            public Map<String, Object> sign(Map<String, Object> map, String signKey) {
                                return SignUtil.sign(map, signKey);
                            }

                            @Override
                            public String sign(String source, String signKey) {
                                return SignUtil.sign(source, signKey);
                            }

                            @Override
                            public String getSignKey() {
                                return BuildConfig.SECRET_KEY;
                            }
                        };
                    }
                }).
                setLog(true).
                setHeaders(hashMap);

        RetrofitManager.init(builder.build());
    }
}
