package com.ssf.framework.main.mvvm.vm;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

public class ViewModelUtil {

    public static ViewModelProvider.Factory createFactory(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("未指定ViewModelFactory时使用默认值构造失败,Context为null,请指定ViewModelFactory或初始化VMSetup.init()..");
        }
        return ViewModelProvider.AndroidViewModelFactory.getInstance(VMSetup.getContext());
    }

}
