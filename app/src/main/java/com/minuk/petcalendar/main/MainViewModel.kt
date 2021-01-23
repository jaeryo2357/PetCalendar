package com.minuk.petcalendar.main

import androidx.hilt.lifecycle.ViewModelInject
import com.minuk.petcalendar.base.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class MainViewModel @ViewModelInject constructor() : BaseViewModel() {

    val navigateSubject = BehaviorSubject.createDefault(MainUiType.CALENDAR)
    val toastSubject = BehaviorSubject.create<String>()

    fun changeMainType(type: MainUiType) {
        navigateSubject.onNext(type)
    }

    fun requestToast(message: String) {
        toastSubject.onNext(message)
    }
}