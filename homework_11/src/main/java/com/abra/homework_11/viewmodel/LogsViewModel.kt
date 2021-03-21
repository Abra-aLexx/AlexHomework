package com.abra.homework_11.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abra.homework_11.json_structure.Logs
import com.abra.homework_11.repository.LogsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class LogsViewModel(private val context: Context,
                    private val coroutineScope: CoroutineScope) : ViewModel() {
    private val repository = LogsRepository(context, coroutineScope)
    private val mutableLogsLiveData = MutableLiveData<Logs>()
    val logsLiveData: LiveData<Logs> = mutableLogsLiveData
    fun requestLogs() {
        coroutineScope.launch {
            mutableLogsLiveData.value = repository.getLogs()
        }
    }
}