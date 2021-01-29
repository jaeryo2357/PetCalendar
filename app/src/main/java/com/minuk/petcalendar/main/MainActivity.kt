package com.minuk.petcalendar.main

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseActivity
import com.minuk.petcalendar.databinding.ActivityMainBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initLayout() {
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as? NavHostFragment

        navHostFragment?.let {
            binding.mainNavigation.setupWithNavController(it.navController)
        }
    }
}