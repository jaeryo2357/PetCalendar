package com.minuk.calendar

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CalendarTest {
    lateinit var device: UiDevice

    @Before
    fun startAccountActivityFromHomeScreen() {
        device = UiDevice.getInstance(getInstrumentation())

        //Home Screen에서 시작
        device.pressHome()

        //SaveAccount 앱 실행
        val context: Context = getApplicationContext()
        val intent = context.packageManager
            .getLaunchIntentForPackage(BASIC_PACKAGE)
        intent?.run {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(this)
        }

        //앱이 화면에 표시 될때까지 대기
        device.wait(Until.hasObject(By.pkg(BASIC_PACKAGE).depth(0)), LANCH_TIMEOUT)
    }

    @Test
    fun checkVerticalAxis_changeCalendar() {
        //TODO: 추후 테스트 로직 변경
       device.wait(Until.hasObject(By.pkg(BASIC_PACKAGE).depth(0)), LANCH_TIMEOUT)

        device.findObject(By.res(BASIC_PACKAGE, "change_calendar_btn"))
            .click()
    }

    companion object {
        val BASIC_PACKAGE = "com.minuk.petcalendar"
        val LANCH_TIMEOUT = 10000L
    }
}