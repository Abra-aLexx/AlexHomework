package com.abra.homework_11.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.abra.homework_11.repository.LogsRepository
import com.abra.homework_11.R
import com.abra.homework_11.functions.createFile
import com.abra.homework_11.json_structure.LogData
import com.abra.homework_11.json_structure.Logs
import com.abra.homework_11.observer.LogsManager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CHANNEL_ID = "customServiceChannel"

class CustomService : Service() {
    private val manager = LogsManager.getInstance()
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val jsonPretty = GsonBuilder().setPrettyPrinting().create()
    private lateinit var repository: LogsRepository
    private var logs: Logs = Logs(mutableListOf())
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startServiceForeground()
        repository = LogsRepository(applicationContext, serviceScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        convertDataToJson(intent)
        writeJsonIntoFile()
        return START_STICKY
    }

    private fun startServiceForeground() {
        NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Custom Service")
                .setSmallIcon(R.drawable.ic_android)
                .build().apply {
                    startForeground(1, this)
                }
    }

    private fun convertDataToJson(intent: Intent?) {
        var actionName = ""
        var date = ""
        var time = ""
        intent?.run {
            actionName = intent.getStringExtra("action_name") ?: ""
            date = intent.getStringExtra("date") ?: ""
            time = intent.getStringExtra("time") ?: ""
        }
        logs.logsList.add(LogData(actionName, date, time))
    }

    private fun writeJsonIntoFile() {
        val stringJson: String = jsonPretty.toJson(logs)
        val file = createFile(applicationContext)
        serviceScope.launch {
            withContext(coroutineContext + Dispatchers.IO) {
                file?.writeText(stringJson, Charsets.UTF_8)
            }
        }
        manager.setData(logs.logsList)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Custom Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java)
                    .apply { createNotificationChannel(serviceChannel) }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}