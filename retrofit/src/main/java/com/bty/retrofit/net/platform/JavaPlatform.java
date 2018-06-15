package com.bty.retrofit.net.platform;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by duo.chen on 2017/7/6
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface JavaPlatform {
}
