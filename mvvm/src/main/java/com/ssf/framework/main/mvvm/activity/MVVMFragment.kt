package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/14
 * @describe
 */
abstract class MVVMFragment<T : ViewDataBinding>(
        private val layoutResID: Int,
        // click 列表
        vararg ids: Int = intArrayOf(0)
) : SupportVMFragment<T>(layoutResID, *ids) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModelProvider: ViewModelProvider by lazy {
        createViewModelProvider()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 注入
        AndroidSupportInjection.inject(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun createViewModelProvider(): ViewModelProvider {
        //从注入的ViewModelFactory获取
        return SuperViewModelProvider(this, viewModelFactory)
    }
}