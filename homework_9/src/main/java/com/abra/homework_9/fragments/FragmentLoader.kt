package com.abra.homework_9.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentLoader {
    fun loadFragment(fragmentClass: Class<out Fragment>, bundle: Bundle)
    fun loadDialogFragment()
}