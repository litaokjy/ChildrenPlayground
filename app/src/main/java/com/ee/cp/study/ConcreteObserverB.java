package com.ee.cp.study;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

public class ConcreteObserverB  implements Observer {
    private String name;

    public ConcreteObserverB(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.e("ConcreteObserver","观察者" + name + "---->" + arg.toString());
    }
}
