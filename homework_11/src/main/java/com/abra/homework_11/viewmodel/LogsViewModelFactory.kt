package com.abra.homework_11.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope

class LogsViewModelFactory(private val context: Context,
                           private val coroutineScope: CoroutineScope) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogsViewModel::class.java)) {
            return LogsViewModel(context = context,
                    coroutineScope = coroutineScope) as T
        }
        throw IllegalArgumentException("Unknown class for the requested ViewModel")
    }
}