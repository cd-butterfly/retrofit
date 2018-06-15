package com.bty.retrofit.net.serurity.sign;

import com.bty.retrofit.net.serurity.Sign;

/**
 * Created by duo.chen on 2018/6/13
 */
public class DefaultSignFactory extends Sign.Factory {

    String key;

    public DefaultSignFactory(String key) {
        this.key = key;
    }

    public static DefaultSignFactory create(String key){
        return new DefaultSignFactory(key);
    }

    @Override
    public Sign get() {
        return new DefaultSign(key);
    }
}
