package com.ee.cp.http;

import android.content.Context;
import android.os.Handler;

import com.ee.cp.NetworkCallbackFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    private OkHttpClient client;
    private static OkHttpUtils instance;

    public OkHttpUtils() {
        client = new OkHttpClient();
    }

    public static OkHttpUtils get() {
        if (instance == null) {
            instance = new OkHttpUtils();
        }
        return instance;
    }


    public void post(final String url, String token, final Map<String, String> params, final NetworkCallback callback) {
        post(null, url, token, params, callback);
    }

    public void post(Context context, final String url, String token, final Map<String, String> params, final NetworkCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request;
        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("token", token)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("token", token)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }


    public void get(final String url, String token, final Map<String, String> params, final NetworkCallback callback) {
        get(null, url, token, params, callback);
    }

    public void get(Context context, final String url, String token, final Map<String, String> params, final NetworkCallback callback) {
        //拼接url
        StringBuilder get_url = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                } else {
                    get_url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        Request request;
        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("token", token)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("token", token)
                    .tag(context)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }


    public void uploadFiles(String url, Map<String, File> files, final NetworkCallback callback) {
        uploadFiles(null, url, null, files, callback);
    }


    public void uploadFiles(String url, Map<String, String> params, Map<String, File> files, final NetworkCallback callback) {
        uploadFiles(null, url, params, files, callback);
    }


    public void uploadFiles(Context context, String url, Map<String, File> files, final NetworkCallback callback) {
        uploadFiles(context, url, null, files, callback);
    }

    public void uploadFiles(Context context, String url, Map<String, String> params, Map<String, File> files, final NetworkCallback callback) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, Objects.requireNonNull(params.get(key))));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                assert file != null;
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        Request request;
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .tag(context)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }


    public void downloadFiles(String url, final NetworkCallbackFile callback) {
        downloadFiles(null, url, callback);
    }

    public void downloadFiles(Context context, String url, final NetworkCallbackFile callback) {
        Request request;
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }


    public void cancel(Context context) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (Objects.equals(call.request().tag(), context))
                    call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (Objects.equals(call.request().tag(), context))
                    call.cancel();
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
