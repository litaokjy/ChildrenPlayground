package com.ee.cp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class NetworkCallbackFile implements Callback {

    private String destFileDir;
    private String destFileName;

    public NetworkCallbackFile(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public abstract void onError(Call call, String e);

    public abstract void onResponse(File response, int id);

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        onError(call, e.toString());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        onResponse(saveFile(response), response.code());
    }

    public File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            assert response.body() != null;
            is = response.body().byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;

        } finally {
            try {
                assert response.body() != null;
                response.body().close();
                if (is != null) is.close();
            } catch (IOException ignored) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException ignored) {
            }

        }
    }
}
