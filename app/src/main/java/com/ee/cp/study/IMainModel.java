package com.ee.cp.study;

import android.content.Context;

import java.util.List;

public interface IMainModel {
    void loadMessage(OnRequestListener<List<String>> list);
}
