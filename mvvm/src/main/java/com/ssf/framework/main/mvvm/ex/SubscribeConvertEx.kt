package com.ssf.framework.main.mvvm.ex

import com.ssf.framework.main.mvvm.vm.BaseViewModel
import com.ssf.framework.main.mvvm.vm.VMSetup
import com.ssf.framework.net.common.ProgressSubscriber
import com.ssf.framework.net.common.ResponseListener
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.net.transformer.ConvertSchedulers
import com.ssf.framework.net.transformer.wrapperSchedulers
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

inline fun <T> Observable<T>.wrapper(rx: LifecycleProvider<*>): Observable<T> {
    return this.compose(wrapperSchedulers<T>())
            .compose(rx.bindToLifecycle())
}

/**
 * 针对Observable<T>订阅加的扩展，Framework中封装的convert只针对Observable<Response<T>>
 */
inline fun <T> Observable<T>.subscribeConvert(
        // 生命周期控制
        rx: LifecycleProvider<*>,
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: ((T) -> Unit)? = null,
        // 失败回调
        noinline error: ((Throwable) -> Unit)? = null,
        // 执行完请求后回调,类似finally
        noinline complete: (() -> Unit)? = null,
        message: String = "loading"
) {
    val observer = if (rx is BaseViewModel) {
        object : Observer<T> {

            override fun onSubscribe(d: Disposable) {
                if (iDialog != IDialog.UN_LOADING) {
                    rx.progress.show(message)
                }
            }

            override fun onNext(t: T) {
                rx.progress.hide()
                try {
                    success?.invoke(t)
                } catch (e: Exception) {
                    e.printStackTrace()
                    //业务代码异常
                    onError(e)
                }
            }

            override fun onError(exception: Throwable) {
                rx.progress.hide()
                try {
                    error?.invoke(exception)
                            ?: VMSetup.getInstance().defaultObservableErrorHandle?.handle(exception)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                complete?.invoke()
            }

            override fun onComplete() {
                complete?.invoke()
            }

        }
    } else {
        val activity = if (rx is RxAppCompatActivity) rx else {
            (rx as? RxFragment)?.activity ?: throw IllegalArgumentException("不支持的生命周期绑定者")
        }
        ProgressSubscriber(activity, iDialog = iDialog, responseListener = object :
                ResponseListener<T> {

            override fun onSucceed(data: T) {
                try {
                    success?.invoke(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError(e)
                }
            }

            override fun onError(exception: Throwable) {
                try {
                    error?.invoke(exception)
                            ?: VMSetup.getInstance().defaultObservableErrorHandle?.handle(exception)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                complete?.invoke()
            }

            override fun onComplete() {
                complete?.invoke()
            }
        })
    }

    this.compose(rx.bindToLifecycle())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(observer)
}

inline fun <T> Observable<Response<T>>.subscribeConvert2(
        rx: LifecycleProvider<*>, // 生命周期控制
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: ((T) -> Unit)? = null,
        // 失败回调
        noinline error: ((Throwable) -> Unit)? = null,
        // 执行完请求后回调,类似finally
        noinline complete: (() -> Unit)? = null,
        message: String = "loading",
        retry: Boolean = true) {
    return this.compose(ConvertSchedulers(retry))
            .subscribeConvert(rx, iDialog, success, error, complete, message)
}

inline fun <T> Observable<T>.safeConvert(  // 成功回调
        noinline success: ((T) -> Unit)? = null,
        // 失败回调
        noinline error: ((Throwable) -> Unit)? = null,
        noinline complete: (() -> Unit)? = null) {
    safeSubscribe(object : Observer<T> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: T) {
            success?.invoke(t)
            complete?.invoke()
        }

        override fun onError(e: Throwable) {
            error?.invoke(e)
            complete?.invoke()
        }

    })
}

inline fun <T> Observable<Response<T>>.safeConvert2(
        // 成功回调
        noinline success: ((T) -> Unit)? = null,
        // 失败回调
        noinline error: ((Throwable) -> Unit)? = null,
        noinline complete: (() -> Unit)? = null,
        retry: Boolean = true) {
    compose(ConvertSchedulers(retry))
            .safeSubscribe(object : Observer<T> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: T) {
                    success?.invoke(t)
                    complete?.invoke()
                }

                override fun onError(e: Throwable) {
                    error?.invoke(e)
                    complete?.invoke()
                }

            })
}