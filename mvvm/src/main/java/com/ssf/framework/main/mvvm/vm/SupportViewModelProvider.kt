package com.ssf.framework.main.mvvm.vm

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStoreOwner
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.ssf.framework.main.mvvm.ob.DefaultActivityObserver
import com.ssf.framework.main.mvvm.ob.DefaultErrorObserver
import com.ssf.framework.main.mvvm.ob.DefaultProgressObserver
import com.ssf.framework.main.mvvm.ob.DefaultToastObserver

/**
 * 为ViewModel提供Progress\toast\error等行为
 */
open class SupportViewModelProvider(private val owner: ViewModelStoreOwner, factory: Factory) : ViewModelProvider(owner, factory) {
    private lateinit var activity: FragmentActivity
    //各个工程自定义风格的处理者
    private var mCustomObserverProvider: IObserverProvider? = null

    init {
        if (owner is FragmentActivity) {
            activity = owner
        } else if (owner is Fragment) {
            activity = owner.activity!!
        }
    }

    //置为全局是为了同个ViewModelStore作用于相同的对象
    private val defaultProgressObserver by lazy {
        VMSetup.getInstance().defaultObserverProvider?.providerProgressObserver(activity)
                ?: DefaultProgressObserver(activity)
    }
    private val defaultToastObserver by lazy {
        VMSetup.getInstance().defaultObserverProvider?.providerToastObserver(activity)
                ?: DefaultToastObserver(activity)
    }
    private val defaultErrorObserver by lazy {
        VMSetup.getInstance().defaultObserverProvider?.providerErrorObserver(activity)
                ?: DefaultErrorObserver(activity)
    }
    private val defaultActivityObserver by lazy { DefaultActivityObserver(activity) }

    override fun <T : ViewModel?> get(key: String, modelClass: Class<T>): T {
        //get viewModel from ownerViewModelStore
        val vm = super.get(key, modelClass)//从ViewModelStore中取出ViewModel
        if (vm is BaseViewModel) {
            //如果是BaseViewModel处理一些默认监听，因为是Activity内共享的，监听时要考虑多个VM可能会造成的冲突
            //如果自定义的Observer提供者中有提供自己的处理就使用提供者的，否则就用默认的
            (owner as? LifecycleOwner)?.let { lifecycleOwner ->
                vm.progress.observeEvent(lifecycleOwner, mCustomObserverProvider?.providerProgressObserver(activity)
                        ?: defaultProgressObserver)
                vm.toast.observeEvent(lifecycleOwner, mCustomObserverProvider?.providerToastObserver(activity)
                        ?: defaultToastObserver)
                vm.error.defaultObserver(mCustomObserverProvider?.providerErrorObserver(activity)
                        ?: defaultErrorObserver)
                vm.activity.observeEvent(lifecycleOwner, defaultActivityObserver)
            }
        }
        return vm
    }

    open fun setCustomObserverProvider(provider: IObserverProvider) {
        this.mCustomObserverProvider = provider
    }
}