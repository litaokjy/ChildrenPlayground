package com.ee.cp.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ee.cp.CPApplication;
import com.ee.cp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private SparseArray<View> views;

    private BaseHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        views = new SparseArray<>();
    }

    static <T extends BaseHolder> T getHolder(Context context, ViewGroup parent, int layoutId) {
        return (T) new BaseHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    //获取view
    private <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return itemView;
    }

    //设置点击事件监听
    public BaseHolder setOnclickListioner(int viewId, View.OnClickListener onClickListener) {
        getView(viewId).setOnClickListener(onClickListener);
        return this;
    }

    public BaseHolder setText(int viewId, String descrp) {
        ((TextView) getView(viewId)).setText(descrp);
        return this;
    }

    public BaseHolder setText(int viewId, int resId) {
        ((TextView) getView(viewId)).setText(resId);
        return this;
    }

    public BaseHolder setImageView(int imageViewId, String url) {
        Glide.with(CPApplication.getContext())
                .load(url)
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into((ImageView) getView(imageViewId));
        return this;
    }

    public BaseHolder setImageView(int imageViewId, int resId) {
        ((ImageView) getView(imageViewId)).setImageResource(resId);
        return this;
    }

    public BaseHolder setImageView(int imageViewId, Bitmap bitmap) {
        ((ImageView) getView(imageViewId)).setImageBitmap(bitmap);
        return this;
    }
}
