package com.z7dream.lib.selector.utils.rx


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-09-03
 * Time: 11:16
 * FIXME
 * 处理Rx线程
 */
object RxSchedulersHelper {

    //处理Rx线程
    fun <T> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .onTerminateDetach()
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
        }
    }

    fun <T> main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
        }
    }


    fun <T> io(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .onTerminateDetach()
                    .observeOn(Schedulers.io())
                    .onTerminateDetach()
        }
    }

    fun <T> fio_main(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .onTerminateDetach()
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
        }
    }

    fun <T> fmain(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach()
        }
    }

    fun <T> fio(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .onTerminateDetach()
                    .observeOn(Schedulers.io())
                    .onTerminateDetach()
        }
    }
}
