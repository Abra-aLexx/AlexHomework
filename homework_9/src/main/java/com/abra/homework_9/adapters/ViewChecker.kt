package com.abra.homework_9.adapters

import android.view.View
import android.widget.ImageView

class ViewChecker {
    private var lastItemChecked: ImageView? = null

    companion object {
        private val checker = ViewChecker()
        fun getInstance() = checker
    }

    fun setVisibility(view: ImageView?) {
        lastItemChecked?.run {
            visibility = View.INVISIBLE
        }
        lastItemChecked = view
    }
}