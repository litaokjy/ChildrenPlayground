package com.ee.cp;


import android.content.Intent;
import android.util.Log;

import com.ee.cp.study.BaseActivity;
import com.ee.cp.study.MainContract;
import com.ee.cp.study.MainPresenter;
import com.ee.cp.study.MyAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ee.cp.R.drawable.ic_launcher;


public class MainActivity extends BaseActivity<MainContract.IMainView, MainPresenter> implements MainContract.IMainView {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        MyAdapter adapter = new MyAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);//设置管理器
        recyclerView.setAdapter(adapter);
        CollapsingToolbarLayout ctl = findViewById(R.id.ctl);
        ctl.setContentScrim(getResources().getDrawable(R.drawable.ic_launcher));
    }

    @Override
    protected void initData() {
        presenter.requestData();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showData(List<String> list) {
        //显示信息
    }

    @Override
    public void showError() {
        //错误提示
    }
}
