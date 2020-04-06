package com.ee.cp.base;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    private Context context;
    private List<T> data;
    private int[] layoutIds;

    //不使用多种布局时的构造函数
    public BaseAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutIds = new int[]{layoutId};
    }

    //使用多种布局时的构造函数
    BaseAdapter(Context context, List<T> data, int[] layoutIds) {
        this.context = context;
        this.data = data;
        this.layoutIds = layoutIds;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    //使用一种布局时的viewType默认为0,使用多种布局时的viewType需要指定
    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return BaseHolder.getHolder(context, viewGroup, layoutIds[viewType]);

    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        onBindData(holder, data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();

    }

    //让子类实现具体的数据绑定方法
    abstract void onBindData(BaseHolder baseHolder, T t, int position);
}
