package com.ssf.framework.main.mvvm.vm

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import java.lang.RuntimeException

/**
 * ViewModel提供者
 *
 * Activity级别内共享ViewModel
 */
class SuperViewModelProvider(private val lifecycleOwner: LifecycleOwner, factory: Factory) : SupportViewModelProvider(
        if (lifecycleOwner is FragmentActivity) {
            lifecycleOwner
        } else if (lifecycleOwner is Fragment && lifecycleOwner.activity != null) {
            lifecycleOwner.activity!!
        } else {//activity is null
            throw RuntimeException("Fragment中创建ViewModel必须在onAttach之后")
        }, factory)