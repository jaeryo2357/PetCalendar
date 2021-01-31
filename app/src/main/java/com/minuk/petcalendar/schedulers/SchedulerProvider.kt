package com.minuk.petcalendar.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SchedulerProvider {

    fun ui(): Scheduler = AndroidSchedulers.mainThread()

    fun computation(): Scheduler = Schedulers.computation()

    fun io(): Scheduler = Schedulers.io()
}