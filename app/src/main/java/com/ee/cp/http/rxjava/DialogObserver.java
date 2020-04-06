package com.ee.cp.http.rxjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class DialogObserver<T> extends BaseObserver<T> {
    private boolean mShowDialog;
    private ProgressDialog dialog;
    private Context mContext;
    private Disposable d;


    private DialogObserver(Context context, Boolean showDialog) {
        mContext = context;
        mShowDialog = showDialog;
    }

    public DialogObserver(Context context) {
        this(context, true);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        } else {
            if (dialog == null && mShowDialog) {
                dialog = new ProgressDialog(mContext);
                dialog.setMessage("正在加载中");
                dialog.show();
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onComplete();
    }

    private void hidDialog() {
        if (dialog != null && mShowDialog)
            dialog.dismiss();
        dialog = null;
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     */
    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
}
