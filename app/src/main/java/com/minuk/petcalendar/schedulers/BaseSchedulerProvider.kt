package com.minuk.petcalendar.schedulers

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun ui() : Scheduler
    fun computation() : Scheduler
    fun io() : Scheduler
}