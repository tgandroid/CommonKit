package com.ssf.framework.main.mvvm.vm;

import org.jetbrains.annotations.Nullable;

public interface IObservableErrorHandle {
    void handle(@Nullable Throwable throwable);
}
