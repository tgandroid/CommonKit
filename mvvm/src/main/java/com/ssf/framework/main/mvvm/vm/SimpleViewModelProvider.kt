package com.ssf.framework.main.mvvm.vm

import android.arch.lifecycle.ViewModelStoreOwner

/**
 * 只存在自身ViewModelStore的ViewModelProvider
 */
open class SimpleViewModelProvider(owner: ViewModelStoreOwner, factory: Factory = ViewModelUtil.createFactory(VMSetup.getContext())) : SupportViewModelProvider(owner, factory)