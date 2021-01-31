package com.minuk.petcalendar.base

import android.os.Bundle
import android.widget.Toast

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: T

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this

        initLayout()

        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    protected open fun initLayout() {}

    protected open fun observeViewModel() {}

    protected fun Disposable.addToDisposable() = addTo(compositeDisposable)

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}