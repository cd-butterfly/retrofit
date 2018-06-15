package com.bty.retrofit.net.convert.fileConvert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import com.bty.retrofit.net.bean.FileUploadRequest;
import com.bty.retrofit.net.serurity.Sign;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by duo.chen on 2017/4/5
 */

public class FileUploadConverterFactory extends Converter.Factory {
    private ParserConfig mParserConfig = ParserConfig.getGlobalInstance();
    private int featureValues = JSON.DEFAULT_PARSER_FEATURE;
    private Sign.Factory factory;

    public static FileUploadConverterFactory create(Sign.Factory factory) {
        return new FileUploadConverterFactory(factory);
    }

    private FileUploadConverterFactory(Sign.Factory factory) {
        this.factory = factory;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (JsonBeanResponse.class.isAssignableFrom((Class<?>) type)) {
            return new FileUploadResponseBodyConverter<JsonBeanResponse>(type, mParserConfig, featureValues);
        } else {
            return null;
        }

    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        if(FileUploadRequest.class.isAssignableFrom((Class<?>) type)) {
            return new FileUploadRequestBodyConverter(factory);
        } else {
            return null;
        }

    }
}
