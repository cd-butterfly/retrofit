package com.bty.retrofit.net.serurity;

import java.util.Map;

/**
 * Created by duo.chen on 2018/6/13
 */
public interface Sign {

    Map<String,Object> sign(Map<String,Object> map, String signKey);

    String sign(String source, String signKey);

    String getSignKey();

    abstract class Factory{

        public Sign get(){
            return null;
        }
    }
}
