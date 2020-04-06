package com.ee.cp.http.retrofit;


import com.ee.cp.http.bean.BaseResponse;
import com.ee.cp.http.bean.RegisterLogin;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NetworkInterface {
    String HOST = "";

    @GET()
    Observable<BaseResponse<String>> getBody(@Url String url);

    @POST()
    Observable<BaseResponse<RegisterLogin>> registerLogin(@Url String url, @Body RequestBody body);

    @POST()
    Call<BaseResponse<RegisterLogin>> post(@Url String url);
}
