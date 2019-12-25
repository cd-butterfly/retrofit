package com.bty.retrofit.net.convert.jsonConvert;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bty.retrofit.net.serurity.Sign;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

final class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private SerializeConfig serializeConfig;
    private SerializerFeature[] serializerFeatures;

    private Sign.Factory factory;


    public FastJsonRequestBodyConverter(SerializeConfig config, SerializerFeature[] features, Sign.Factory factory) {
        serializeConfig = config;
        serializerFeatures = features;
        this.factory = factory;
    }

    @Override
    public RequestBody convert(@NonNull T value) throws IOException {

        byte[] content = new byte[]{};
        String jsonString = null;

        Sign sign = factory.get();

        if (serializeConfig != null) {
            if (serializerFeatures != null) {

                if (sign != null) {
                    String signed = sign.sign(JSON.toJSONString(value,
                            serializeConfig, serializerFeatures),sign.getSignKey());

                    if (!TextUtils.isEmpty(signed)) {
                        jsonString = signed;
                    }
                } else {
                    jsonString = JSON.toJSONString(value,
                            serializeConfig, serializerFeatures);
                }


            } else {
                if (sign != null) {
                    String signed = sign.sign(JSON.toJSONString(value,
                            serializeConfig),sign.getSignKey());

                    if (!TextUtils.isEmpty(signed)) {
                        jsonString = signed;
                    }
                } else {
                    jsonString = JSON.toJSONString(value,
                            serializeConfig);
                }
            }
        } else {
            if (serializerFeatures != null) {
                if (sign != null) {
                    String signed = sign.sign(JSON.toJSONString(value, serializerFeatures),sign.getSignKey());

                    if (!TextUtils.isEmpty(signed)) {
                        jsonString = signed;
                    }
                } else {
                    jsonString = JSON.toJSONString(value, serializerFeatures);
                }

            } else {
                if (sign != null) {
                    String signed = sign.sign(JSON.toJSONString(value),sign.getSignKey());

                    if (!TextUtils.isEmpty(signed)) {
                        jsonString = signed;
                    }
                } else {
                    jsonString = JSON.toJSONString(value);
                }
            }
        }
        if (jsonString != null) {
            content = jsonString.getBytes();
        }

        return RequestBody.create(MEDIA_TYPE, content);
    }
}
