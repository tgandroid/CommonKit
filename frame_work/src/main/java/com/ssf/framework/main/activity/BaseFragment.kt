package com.ssf.framework.main.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xm.xlog.KLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

abstract class BaseFragment(
        private val layoutResID: Int,
        private vararg val ids: Int = intArrayOf(0)
) : RxFragment(), View.OnClickListener {

    /* 主视图 */
    var mInflate: View? = null
    // 是否初始化过
    private var mInit = false

    /**
     * 懒加载标志位
     */
    var isPrepared: Boolean = false //是否已经就绪，但未显示
        private set
    var isLazyLoad: Boolean = false //是否已执行过懒加载
        private set
    var isActive: Boolean = false //是否可交互状态
        private set
    private var backUserVisible: Boolean = false //备份之前的显示状态

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mInflate == null) {
            mInflate = inflater.inflate(layoutResID, container, false)
        }
        // 初始化默认配置
        initDefaultConfig(savedInstanceState)
        // 返回
        return mInflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
    }

    /**
     * 初始化默认配置
     */
    open fun initDefaultConfig(savedInstanceState: Bundle?) {
        if (!mInit) {
            mInit = true
            init(mInflate, savedInstanceState)
            //初始化监听
            setClickViewId(mInflate)
        }
    }

    /** 初始化 */
    abstract fun init(view: View?, savedInstanceState: Bundle?)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {
            invokeUserVisible(true)
            if (canLazyLoad()) {
                isLazyLoad = true
                onLazyLoad()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isPrepared) {
            invokeUserVisible(true)
            if (canLazyLoad()) {
                isLazyLoad = true
                onLazyLoad()
            }
        }
        if (!isVisibleToUser && isActive) {
            invokeUserVisible(false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isActive && userVisibleHint && !backUserVisible) {
            invokeUserVisible(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint && backUserVisible) {
            invokeUserVisible(false)
        }
    }

    private fun invokeUserVisible(visible: Boolean) {
        if (!isPrepared) return
        if (visible) {
            onUserVisible()
            isActive = true
        } else {
            if (isActive && backUserVisible) {
                onUserInVisible()
            }
        }
        backUserVisible = visible //备份状态，用来在Resume和Stop时也能响应invokeVisible
    }

    /**
     * 用户可见
     */
    open protected fun onUserVisible() {

    }

    /**
     * 用户不可见
     */
    open protected fun onUserInVisible() {

    }

    /**
     * 是否允许回调懒加载方法，默认只加载一次,后续可从vm中恢复数据
     */
    open protected fun canLazyLoad(): Boolean {
        return !isLazyLoad
    }

    /**
     * 懒加载数据
     */
    open fun onLazyLoad() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isActive = false
        backUserVisible = false
        isPrepared = false
    }

    /**
     * 简化 findViewById 过程
     */
    fun <T : View> findViewById(@IdRes id: Int): T {
        return mInflate!!.findViewById(id)
    }

    /**
     * 跳转页面
     *
     * @param clazz
     */
    fun startActivity(clazz: Class<out BaseActivity>) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
    }

    protected fun setClickViewId(view: View?) {
        view?.let {
            Observable.create(object : ObservableOnSubscribe<View>, View.OnClickListener {
                lateinit var emitter: ObservableEmitter<View>
                override fun onClick(v: View) {
                    emitter.onNext(v)
                }

                override fun subscribe(emitter: ObservableEmitter<View>) {
                    this.emitter = emitter
                    ids.forEach { id ->
                        if (id != 0) {
                            it.findViewById<View>(id)?.setOnClickListener(this)
                                    ?: KLog.e(this.javaClass.simpleName, "Could not find view by id=$id...")
                        }
                    }
                }
            }).compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe({ onClick(it) })
        }
    }


    override fun onClick(v: View) {
    }

    open fun refresh() {

    }
}