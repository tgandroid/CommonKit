package com.ssf.framework.main.mvvm.ex

import com.ssf.framework.main.mvvm.vm.BaseViewModel
import com.ssf.framework.net.interfac.IDialog
import io.reactivex.Observable
import retrofit2.Response

/**
 * @atuthor dm
 * @data on 2018/9/20
 * @describe
 */


/**
 * 网络请求
 */
@Deprecated("请使用subscribeConvert扩展函数替代", replaceWith = ReplaceWith("subscribeConvert()"))
public inline fun <T> BaseViewModel.apply(
        // 必传对象，用于控制声明周期
        observable: Observable<T>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    observable.subscribeConvert(this, iDialog, success, error, complete, message)
}

@Deprecated("请使用subscribeConvert2扩展函数替代", replaceWith = ReplaceWith("subscribeConvert2()"))
public inline fun <T> BaseViewModel.convert(
        // 必传对象，用于控制声明周期
        observable: Observable<Response<T>>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    observable.subscribeConvert2(this, iDialog, success, error, complete, message, retry)
}


