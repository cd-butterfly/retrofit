package com.bty.retrofit.net.platform;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by duo.chen on 2018/6/13
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface DoNetPlatform {
}
