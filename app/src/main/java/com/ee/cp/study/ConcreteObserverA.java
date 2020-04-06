package com.ee.cp.study;


import android.util.Log;

import java.util.Observable;
import java.util.Observer;

public class ConcreteObserverA implements Observer {
    private String name;

    public ConcreteObserverA(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.e("ConcreteObserver","观察者" + name + "---->" + arg.toString());
    }
}
