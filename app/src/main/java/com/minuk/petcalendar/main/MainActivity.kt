package com.minuk.petcalendar.main

import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseActivity
import com.minuk.petcalendar.calendar.CalendarFragment
import com.minuk.petcalendar.databinding.ActivityMainBinding
import com.minuk.petcalendar.document.DocumentFragment
import com.minuk.petcalendar.setting.SettingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModels()

    override fun initLayout() {
        super.initLayout()

        initBottomNavigationView()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        observeNavigation()
        observeToast()
    }

    private fun initBottomNavigationView() {
        findViewById<BottomNavigationView>(R.id.main_navigation)
            .setOnNavigationItemSelectedListener {
                when(it.itemId) {

                    R.id.action_calendar -> {
                        mainViewModel.changeMainType(MainUiType.CALENDAR)
                        false
                    }

                    R.id.action_document -> {
                        mainViewModel.changeMainType(MainUiType.DOCUMENT)
                        false
                    }

                    R.id.action_setting -> {
                        mainViewModel.changeMainType(MainUiType.SETTING)
                        false
                    }

                    else -> false
                }
            }
    }

    private fun observeNavigation() {
        mainViewModel.navigateSubject
            .subscribe(::navigate)
            .addToDisposable()
    }

    private fun observeToast() {
        mainViewModel.toastSubject
            .subscribe(::showToast)
            .addToDisposable()
    }

    private fun navigate(type: MainUiType) {
        val newFragment = when(type) {
            MainUiType.CALENDAR -> CalendarFragment.newInstance()

            MainUiType.DOCUMENT -> DocumentFragment.newInstance()

            else -> SettingFragment.newInstance()
        }

        val currentFragment = supportFragmentManager.primaryNavigationFragment

        if (currentFragment == null || currentFragment.javaClass != newFragment.javaClass) {
            supportFragmentManager.beginTransaction()
                .setPrimaryNavigationFragment(newFragment)
                .replace(R.id.main_container, newFragment)
                .addToBackStack(newFragment.javaClass.simpleName)
                .commit()
        }
    }

}