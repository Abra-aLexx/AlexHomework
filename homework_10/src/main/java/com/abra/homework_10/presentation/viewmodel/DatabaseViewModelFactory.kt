package com.abra.homework_10.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope

class DatabaseViewModelFactory(private val scope: CoroutineScope) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            return DatabaseViewModel(scope = scope) as T
        }
        throw IllegalArgumentException("Unknown class for the requested ViewModel")
    }
}