package com.minuk.petcalendar.document

import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseFragment
import com.minuk.petcalendar.databinding.FragmentDocumentBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentFragment : BaseFragment<FragmentDocumentBinding>
    (R.layout.fragment_document) {

    companion object {
        fun newInstance() = DocumentFragment()
    }
}