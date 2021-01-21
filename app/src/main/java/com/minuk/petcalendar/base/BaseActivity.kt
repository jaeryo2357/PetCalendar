package com.minuk.petcalendar.base

import android.os.Bundle
import android.widget.Toast

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

open class BaseActivity<T: ViewDataBinding>(@LayoutRes val layoutRes: Int): AppCompatActivity() {

    protected lateinit var binding: T

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }

    open fun initLayout() { }

    open fun observeViewModel() { }

    protected fun Disposable.addToDisposable() = addTo(compositeDisposable)

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}