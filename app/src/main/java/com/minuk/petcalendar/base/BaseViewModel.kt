package com.minuk.petcalendar.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

open class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()

        onViewModelCleared()
        compositeDisposable.clear()
    }

    protected open fun onViewModelCleared() {}

    protected fun Disposable.addToDisposable() = addTo(compositeDisposable)
}