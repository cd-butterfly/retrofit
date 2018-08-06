package com.bty.retrofit.net.serurity.sign;


import com.bty.retrofit.net.serurity.Sign;

import java.util.Map;

/**
 * Created by duo.chen on 2018/6/13
 */
public class DefaultSign implements Sign {

    private String key;

    public DefaultSign(String key) {
        this.key = key;
    }

    @Override
    public Map<String, Object> sign(Map map, String s) {
        return map;
    }

    @Override
    public String sign(String source, String signKey) {
        return source;
    }

    @Override
    public String getSignKey() {
        return key;
    }
}
