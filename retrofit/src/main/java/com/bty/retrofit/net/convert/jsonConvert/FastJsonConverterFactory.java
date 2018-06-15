package com.bty.retrofit.net.convert.jsonConvert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bty.retrofit.net.bean.JsonBeanRequest;
import com.bty.retrofit.net.bean.JsonBeanResponse;
import com.bty.retrofit.net.platform.DoNetPlatform;
import com.bty.retrofit.net.platform.JavaPlatform;
import com.bty.retrofit.net.serurity.Sign;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * modify from org.ligboy.retrofit2:converter-fastjson
 * <p>
 * A {@linkplain Converter.Factory converter} which uses FastJson for JSON.
 * <p>
 * Because FastJson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add
 * this instance} last to allow the other converters a chance to see their types.
 */
public class FastJsonConverterFactory extends Converter.Factory {

    private int featureValues = JSON.DEFAULT_PARSER_FEATURE;
    private Feature[] features;

    private SerializerFeature[] serializerFeatures;
    private Sign.Factory factory;

    private FastJsonConverterFactory(Sign.Factory factory) {
        this.factory = factory;
    }

    /**
     * Create an default instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     *
     * @return The instance of FastJsonConverterFactory
     */
    public static FastJsonConverterFactory create(Sign.Factory factory) {
        return new FastJsonConverterFactory(factory);
    }

     /*
        //config FastJson
        //Wiki https://github.com/alibaba/fastjson/wiki/PropertyNamingStrategy_cn
        ------------------------------
        |   name	 |      demo     |
        | CamelCase	 |    persionId  |
        | PascalCase |	  PersonId   |
        | SnakeCase	 |    person_id  |
        | KebabCase	 |    person-id  |
        ------------------------------
        */

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (JsonBeanResponse.class.isAssignableFrom((Class<?>) type)) {

            for (Annotation annotation : annotations) {
                if (annotation instanceof JavaPlatform) {
                    ParserConfig mParserConfig = new ParserConfig();
                    mParserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
                    return new FastJsonResponseBodyConverter<JsonBeanResponse>
                            (type, mParserConfig, featureValues, features);
                }
            }

            ParserConfig mParserConfig = ParserConfig.getGlobalInstance();
            //ParserConfig mParserConfig = new ParserConfig();
            //mParserConfig.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
            return new FastJsonResponseBodyConverter<JsonBeanResponse>
                    (type, mParserConfig, featureValues, features);
        } else {
            return null;
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (JsonBeanRequest.class.isAssignableFrom((Class<?>) type)) {

            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof DoNetPlatform) {
                    SerializeConfig serializeConfig = new SerializeConfig();
                    serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
                    return new FastJsonRequestBodyConverter<JsonBeanRequest>(serializeConfig, serializerFeatures,factory);
                } else if (annotation instanceof JavaPlatform) {
                    SerializeConfig serializeConfig = new SerializeConfig();
                    serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
                    return new FastJsonRequestBodyConverter<JsonBeanRequest>(serializeConfig, serializerFeatures,factory);
                }
            }

            SerializeConfig serializeConfig = new SerializeConfig();
            serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
            return new FastJsonRequestBodyConverter<JsonBeanRequest>(serializeConfig, serializerFeatures,factory);
        } else {
            return null;
        }
    }

    public int getParserFeatureValues() {
        return featureValues;
    }

    public FastJsonConverterFactory setParserFeatureValues(int featureValues) {
        this.featureValues = featureValues;
        return this;
    }

    public Feature[] getParserFeatures() {
        return features;
    }

    public FastJsonConverterFactory setParserFeatures(Feature[] features) {
        this.features = features;
        return this;
    }

    public SerializerFeature[] getSerializerFeatures() {
        return serializerFeatures;
    }

    public FastJsonConverterFactory setSerializerFeatures(SerializerFeature[] features) {
        this.serializerFeatures = features;
        return this;
    }
}
