package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import com.ssf.framework.main.activity.BaseActivity
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider

/**
 * MVVM模式的Fragment，可以不使用Dagger2
 */
abstract class SupportVMActivity<T : ViewDataBinding>(
        // 自定义布局
        private val layoutResID: Int,
        //需要设置点击事件的ViewId
        vararg ids: Int,
        // 是否可以滑动退出，默认true
        swipeBackLayoutEnable: Boolean = true,
        // StatusBar颜色
        statusBarColor: Int = 0,
        // StatusBar 透明度 (0 - 255)
        statusBarAlpha: Int = 0
) : BaseActivity(layoutResID, *ids, swipeBackLayoutEnable = swipeBackLayoutEnable, statusBarColor = statusBarColor, statusBarAlpha = statusBarAlpha) {

    // mvvm
    protected lateinit var binding: T

    val viewModelProvider: ViewModelProvider by lazy {
        createViewModelProvider()
    }

    override fun setContentView() {
        // 初始化 Binding
        binding = DataBindingUtil.setContentView(this, layoutResID)
        binding.setLifecycleOwner(this)
    }

    /** 回收资源 */
    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    open protected fun createViewModelProvider(): ViewModelProvider {
        return SuperViewModelProvider(this)
    }
}