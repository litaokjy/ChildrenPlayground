package com.ee.cp.http.retrofit;


import com.ee.cp.CPApplication;
import com.ee.cp.utils.LogUtils;
import com.ee.cp.utils.NetworkUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {
    private static final int DEFAULT_TIMEOUT = 10;//超时时间
    private static volatile NetworkInterface networkInterface = null;

    /**
     * 创建网络请求接口实例
     */
    public static NetworkInterface getRequestInterface() {
        if (networkInterface == null) {
            synchronized (NetworkInterface.class) {
                networkInterface = provideRetrofit().create(NetworkInterface.class);
            }
        }
        return networkInterface;
    }

    /**
     * 初始化必要对象和参数
     */
    private static Retrofit provideRetrofit() {
        File cacheFile = new File(CPApplication.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache) //设置缓存
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(getRequestHeader())
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(commonParamsInterceptor())
                .addInterceptor(getResponseHeader())
                .addInterceptor(getCacheInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkInterface.HOST)//设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create())//设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava平台
                .client(client)
                .build();
        return retrofit;
    }

    /**
     * 日志拦截器
     */
    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtils.Le("OkHttp", "log = " + message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


    /**
     * 请求头拦截器
     */
    private static Interceptor getRequestHeader() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                //使用addHeader()不会覆盖之前设置的header,若使用header()则会覆盖之前的header
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Type", "application/json; charset=utf-8");
                builder.addHeader("token", "d08986536b5e3678119aac9b892439a8");
                builder.removeHeader("User-Agent");
                builder.method(originalRequest.method(), originalRequest.body());
                Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 统一的请求参数拦截器
     */
    private static Interceptor commonParamsInterceptor() {
        Interceptor commonParams = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originRequest = chain.request();
                Request request;
                HttpUrl httpUrl = originRequest.url().newBuilder().
                        addQueryParameter("paltform", "android").
                        addQueryParameter("version", "1.0.0").build();
                request = originRequest.newBuilder().url(httpUrl).build();
                return chain.proceed(request);
            }
        };
        return commonParams;
    }

    /**
     * 通过响应拦截器实现了从响应中获取服务器返回的time
     */
    private static Interceptor getResponseHeader() {
        Interceptor interceptor = new Interceptor() {
            @NonNull
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                String timestamp = response.header("time");
                if (timestamp != null) {
                    //获取到响应header中的time
                }
                return response;
            }
        };
        return interceptor;
    }

    /**
     * 在无网络的情况下读取缓存，有网络的情况下根据缓存的过期时间重新请求
     */
    private static Interceptor getCacheInterceptor() {
        Interceptor commonParams = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.isConnected()) {
                    //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                okhttp3.Response response = chain.proceed(request);
                if (NetworkUtils.isConnected()) {//有网络情况下，根据请求接口的设置，配置缓存。
                    //这样在下次请求时，根据缓存决定是否真正发出请求。
                    String cacheControl = request.cacheControl().toString();
                    //当然如果你想在有网络的情况下都直接走网络，那么只需要
                    //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
                    int maxAge = 60 * 60; // read from cache for 1 minute
                    return response.newBuilder()
//                            .header("Cache-Control", cacheControl)
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    //无网络
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return response.newBuilder()
                            .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }

            }
        };
        return commonParams;
    }
}
