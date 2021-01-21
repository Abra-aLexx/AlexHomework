package com.abra.homework_7_coroutines.scopes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DatabaseCoroutineScope {
    companion object {
        private val SCOPE = CoroutineScope(Dispatchers.Main + Job())
        fun getInstance() = SCOPE
    }
}