package com.hermesjunior.imagesearcher.ui.customview

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    open fun onBackPressed() : Boolean = false
}
