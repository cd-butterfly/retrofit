package com.bty.annotation;

/**
 * Created by duo.chen on 2018/6/12
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target(TYPE)
@Retention(CLASS)
public @interface Service {
    /**
     *
     * default name is the class name
     *
     */
    String alias() default "";

    /**
     *
     * base url,if not set,default baseUrl is config's baseUrl
     *
     */
    String baseUrl() default "";
}
