package com.ssf.framework.main.mvvm.vm;

import android.app.Application;
import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * VM全局配置
 */
public class VMSetup {
    private static VMSetup INSTANCE = new VMSetup();
    private static Application mContext;

    private VMSetup() {
    }

    public static VMSetup getInstance() {
        return INSTANCE;
    }

    public static void init(@Nullable Context context) {
        if (context == null) return;
        mContext = ((Application) context.getApplicationContext());
    }

    @Nullable
    private IObserverProvider defaultObserverProvider;

    @Nullable
    private IObservableErrorHandle defaultObservableErrorHandle;

    public void setDefaultObserverProvider(@NotNull IObserverProvider provider) {
        this.defaultObserverProvider = provider;
    }

    public void setDefaultObservableErrorHandle(@NotNull IObservableErrorHandle handler) {
        this.defaultObservableErrorHandle = handler;
    }

    @Nullable
    public IObserverProvider getDefaultObserverProvider() {
        return defaultObserverProvider;
    }

    @Nullable
    public IObservableErrorHandle getDefaultObservableErrorHandle() {
        return defaultObservableErrorHandle;
    }

    public static Application getContext() {
        return mContext;
    }
}
