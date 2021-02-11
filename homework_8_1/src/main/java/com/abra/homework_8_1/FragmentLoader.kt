package com.abra.homework_8_1

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentLoader {
    fun loadFragment(fragmentClass: Class<out Fragment>, transitionCode: Int, bundle: Bundle)
    fun loadFragment(fragment: Fragment, transitionCode: Int)
}