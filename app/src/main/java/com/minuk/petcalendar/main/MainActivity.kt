package com.minuk.petcalendar.main

import android.util.Log

import androidx.activity.viewModels
import androidx.fragment.app.commit

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
        binding.mainNavigation.setOnNavigationItemSelectedListener {
            val mainUiType = when (it.itemId) {

                R.id.action_calendar -> MainUiType.CALENDAR

                R.id.action_document -> MainUiType.DOCUMENT

                else -> MainUiType.SETTING
            }

            mainViewModel.changeMainType(mainUiType)
            true
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
        val newFragment = supportFragmentManager.findFragmentByTag(type.tag)
            ?: getFragment(type)

        val currentFragment = supportFragmentManager.primaryNavigationFragment

        if (currentFragment == null || currentFragment.javaClass != newFragment.javaClass) {
            supportFragmentManager.commit {
                setPrimaryNavigationFragment(newFragment)
                replace(R.id.main_container, newFragment, type.tag)
                addToBackStack(null)
            }
        }

        Log.d("test", supportFragmentManager.backStackEntryCount.toString())
    }

    private fun getFragment(type: MainUiType) = when (type) {
        MainUiType.CALENDAR -> CalendarFragment.newInstance()

        MainUiType.DOCUMENT -> DocumentFragment.newInstance()

        else -> SettingFragment.newInstance()
    }

    /**
     *  BackStack에 쌓인 Fragment 상관없이 뒤로 가기 버튼 누르면 바로 종료
     */
    override fun onBackPressed() {
        finish()
    }
}