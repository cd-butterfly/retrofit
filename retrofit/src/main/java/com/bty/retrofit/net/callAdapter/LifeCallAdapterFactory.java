package com.bty.retrofit.net.callAdapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bty.retrofit.net.bean.JsonBeanResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by duo.chen on 2017/5/10
 */

public class LifeCallAdapterFactory extends CallAdapter.Factory {

    public static LifeCallAdapterFactory create() {
        return new LifeCallAdapterFactory();
    }

    @Override
    public CallAdapter<JsonBeanResponse, LifeCall> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType == Call.class && returnType instanceof ParameterizedType) {
            final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

            return new CallAdapter<JsonBeanResponse, LifeCall>() {
                @Override
                public Type responseType() {
                    return responseType;
                }

                @Override
                public LifeCallAdapterFactory.LifeCall adapt(@NonNull Call<JsonBeanResponse> call) {
                    return new LifeCallAdapterFactory.LifeCall<>(MainThreadExecutor.create(), call);
                }
            };
        } else {
            return null;
        }
    }

    static class MainThreadExecutor implements Executor {

        public static MainThreadExecutor create() {
            return new MainThreadExecutor();
        }

        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable r) {
            handler.post(r);
        }
    }


    static class LifeCall<T extends JsonBeanResponse> implements Call<T>, LifecycleObserver {

        final Executor callbackExecutor;
        final Call<T> delegate;

        public LifeCall(Executor callbackExecutor, Call<T> delegate) {
            this.callbackExecutor = callbackExecutor;
            this.delegate = delegate;
        }

        public Response<T> execute() throws IOException {
            return delegate.execute();
        }

        @Override
        public void enqueue(@NonNull final Callback<T> callback) {

            if (callback instanceof LifeCallback) {
                LifeCallback lifeCallback = (LifeCallback) callback;
                if (lifeCallback.getLifecycleOwner() != null) {
                    lifeCallback.getLifecycleOwner().getLifecycle().addObserver(this);
                }
            }

            delegate.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull final Response<T> response) {
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (delegate.isCanceled()) {
                                // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
                                callback.onFailure(LifeCall.this, new IOException("Canceled"));
                            } else {
                                callback.onResponse(LifeCall.this, response);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull final Throwable t) {
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(LifeCall.this, t);
                        }
                    });
                }
            });
        }

        @Override
        public boolean isExecuted() {
            return delegate.isExecuted();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public boolean isCanceled() {
            return delegate.isCanceled();
        }

        @Override
        public Call<T> clone() {
            return new LifeCall<>(callbackExecutor, delegate.clone());
        }

        @Override
        public Request request() {
            return delegate.request();
        }

    }

    public static abstract class LifeCallback<T> implements Callback<T> {

        private WeakReference<LifecycleOwner> lifecycleOwnerWR;

        public LifeCallback(@Nullable LifecycleOwner lifecycleOwner) {
            this.lifecycleOwnerWR = new WeakReference<>(lifecycleOwner);
        }

        public LifecycleOwner getLifecycleOwner() {
            return lifecycleOwnerWR.get();
        }
    }

}
