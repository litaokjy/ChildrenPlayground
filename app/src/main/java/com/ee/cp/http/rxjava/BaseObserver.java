package com.ee.cp.http.rxjava;

import com.ee.cp.http.bean.BaseResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public void onNext(@NonNull BaseResponse<T> tBaseResponse) {
        if (tBaseResponse.getRes_code() == 200) {
            onSuccess(tBaseResponse.getDemo());
        } else {
            onFailure(null, tBaseResponse.getErr_msg());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        try {
            if (e instanceof SocketTimeoutException) {//请求超时
                onFault(e, "请求超时,请稍后再试");
            } else if (e instanceof ConnectException) {//网络连接超时
                onFault(e, "网络连接超时,请检查网络状态");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                onFault(e, "安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    onFault(e, "网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    onFault(e, "请求的地址不存在");
                } else {
                    onFault(e, "请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                onFault(e, "域名解析失败");
            } else {
                onFault(e, "error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T demo);

    public abstract void onFailure(Throwable e, String errorMsg);

    public abstract void onFault(Throwable e, String errorMsg);
}
