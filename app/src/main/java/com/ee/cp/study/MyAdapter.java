package com.ee.cp.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ee.cp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater inflater;

    public MyAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    //创建ViewHolder
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view, parent, false);
        MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
        return viewHolder;
    }

    //向item中添加数据
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
    }

    //返回item个数
    @Override
    public int getItemCount() {
        return 30;
    }

    //填充视图、设置点击事件。
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
