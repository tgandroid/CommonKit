package com.ssf.framework.main.mvvm.vm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * VM全局配置
 */
public class VMSetup {
    private static VMSetup INSTANCE = new VMSetup();

    private VMSetup() {
    }

    public static VMSetup getInstance() {
        return INSTANCE;
    }

    @Nullable
    private IObserverProvider defaultObserverProvider;

    @Nullable
    private IObservableErrorHandle defaultObservableErrorHandle;

    public void setDefaultObserverProvider(@NotNull IObserverProvider provider) {
        this.defaultObserverProvider = provider;
    }

    public void setDefaultObserverProvider(@NotNull IObservableErrorHandle provider) {
        this.defaultObservableErrorHandle = provider;
    }

    @Nullable
    public IObserverProvider getDefaultObserverProvider() {
        return defaultObserverProvider;
    }

    @Nullable
    public IObservableErrorHandle getDefaultObservableErrorHandle() {
        return defaultObservableErrorHandle;
    }
}
