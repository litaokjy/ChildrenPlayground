package com.ee.cp.study;

import java.util.List;

public class MainPresenter extends MainContract.IMainPresenter {
    private IMainModel model = new MainModelImpl();

    @Override
    public void requestData() {
        final MainContract.IMainView view = getView();
        model.loadMessage(strings -> {
            if (strings.size() > 0) {
                view.showData(strings);
            } else {
                view.showError();
            }
        });
    }
}
