package com.ee.cp.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyObserver extends Observable {
    private static MyObserver myObserver;
    private List<Observer> list = new ArrayList<>();

    public static MyObserver getInstance() {
        if (null == myObserver) {
            synchronized (MyObserver.class) {
                if (null == myObserver) {
                    myObserver = new MyObserver();
                }
            }
        }
        return myObserver;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        list.add(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        list.remove(o);
    }

    @Override
    public synchronized void deleteObservers() {
        list.clear();
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer observer : list) {
            observer.update(null, arg);
        }
    }
}
