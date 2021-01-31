package com.minuk.petcalendar.setting

import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseFragment
import com.minuk.petcalendar.databinding.FragmentSettingBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    companion object {
        fun newInstance() = SettingFragment()
    }
}