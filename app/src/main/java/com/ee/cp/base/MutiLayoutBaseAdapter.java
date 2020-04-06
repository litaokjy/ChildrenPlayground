package com.ee.cp.base;

import android.content.Context;

import java.util.List;

public abstract class MutiLayoutBaseAdapter<T> extends BaseAdapter<T> {
    public MutiLayoutBaseAdapter(Context context, List<T> data, int[] layoutIds) {
        super(context, data, layoutIds);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }

    @Override
    protected void onBindData(BaseHolder baseHolder, T t, int position) {
        onBinds(baseHolder, t, position, getItemViewType(position));
    }

    //子类实现得到具体的子类布局的方法
    abstract int getItemType(int position);

    //子类实现对不同的item进行操作
    abstract void onBinds(BaseHolder baseHolder, T t, int position, int itemViewType);
}
