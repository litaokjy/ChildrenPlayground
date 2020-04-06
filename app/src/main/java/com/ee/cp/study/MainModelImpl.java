package com.ee.cp.study;


import java.util.ArrayList;
import java.util.List;

public class MainModelImpl implements IMainModel {
    private List<String> list;

    @Override
    public void loadMessage(OnRequestListener<List<String>> onRequestListener) {
        if (list == null)
            list = new ArrayList<>();
        list.add("测试信息1");
        list.add("测试信息2");
        list.add("测试信息3");
        list.add("测试信息4");
        list.add("测试信息5");
        onRequestListener.onSuccess(list);
    }
}
