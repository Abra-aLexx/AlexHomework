package com.abra.homework_10.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentLoader {
    fun loadFragment(fragmentClass: Class<out Fragment>, bundle: Bundle)
    fun loadDialogFragment()
}