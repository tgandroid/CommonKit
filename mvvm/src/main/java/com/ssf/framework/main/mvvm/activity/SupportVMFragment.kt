package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssf.framework.main.activity.BaseFragment
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider

/**
 * MVVM模式的Fragment，可以不使用Dagger2
 */
abstract class SupportVMFragment<T : ViewDataBinding>(
        private val layoutResID: Int,
        // click 列表
        vararg ids: Int = intArrayOf(0)) : BaseFragment(layoutResID, *ids) {

    // mvvm
    protected lateinit var binding: T

    val viewModelProvider: ViewModelProvider by lazy {
        createViewModelProvider()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // dataBinding
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false)
        binding.setLifecycleOwner(this)
        // 记录布局
        mInflate = binding.root
        // 初始化默认配置
        initDefaultConfig(savedInstanceState)
        // DataBinding
        // 注册
        initClickEvent()
        return mInflate
    }

    override fun initDefaultConfig(savedInstanceState: Bundle?) {
        init(mInflate, savedInstanceState)
        //初始化监听
        setClickViewId(mInflate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    private fun initClickEvent() {
        //初始化监听
        setClickViewId(mInflate)
    }

    protected open fun createViewModelProvider(): ViewModelProvider {
        return SuperViewModelProvider(this)
    }
}