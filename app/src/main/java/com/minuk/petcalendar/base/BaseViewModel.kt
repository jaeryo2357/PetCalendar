package com.minuk.petcalendar.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

    protected fun Disposable.addToDisposable() = addTo(compositeDisposable)
}