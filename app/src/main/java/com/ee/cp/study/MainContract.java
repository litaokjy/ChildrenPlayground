package com.ee.cp.study;

import java.util.List;

public interface MainContract {
    interface IMainView extends BaseView {

        void showData(List<String> list);
    }


    abstract class IMainPresenter extends BasePresenter<IMainView> {

        public abstract void requestData();
    }
}
