package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/5 0005
 * @describe
 */
abstract class MVVMActivity<T : ViewDataBinding>(
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
) : SupportVMActivity<T>(layoutResID, *ids, swipeBackLayoutEnable = swipeBackLayoutEnable, statusBarColor = statusBarColor, statusBarAlpha = statusBarAlpha),
        HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun supportFragmentInjector() = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        // 注入
        AndroidInjection.inject(this)
        // create
        super.onCreate(savedInstanceState)
    }

    override fun createViewModelProvider(): ViewModelProvider {
        //从注入的ViewModelFactory获取
        return SuperViewModelProvider(this, viewModelFactory)
    }

}