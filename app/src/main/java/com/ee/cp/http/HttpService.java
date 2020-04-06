package com.ee.cp.http;

import com.ee.cp.http.bean.BaseResponse;
import com.ee.cp.http.bean.RegisterLogin;
import com.ee.cp.http.retrofit.RetrofitUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;

public class HttpService {

    public static Observable<BaseResponse<RegisterLogin>> registerLogin(String phone, String code) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("code", code);
        String strJson = gson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), strJson);
        return RetrofitUtils.getRequestInterface()
                .registerLogin("", body)
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }
}
