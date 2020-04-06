package com.ee.cp.http;


import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class NetworkCallback implements Callback {


    public abstract void onError(Call call, String e);

    public abstract void onResponse(JSONObject response, int id);

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        onError(call, e.toString());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        assert response.body() != null;
        final String response_body = response.body().string();
        try {
            JSONObject jsonObject = new JSONObject(response_body);
            onResponse(jsonObject, response.code());
        } catch (JSONException e) {
            onError(call, e.toString());
        }
    }
}
