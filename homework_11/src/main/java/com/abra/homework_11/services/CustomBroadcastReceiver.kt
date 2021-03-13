package com.abra.homework_11.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date

class CustomBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context, intent: Intent) {
        val data = Intent(context, CustomService::class.java)
                .apply {
                    putExtra("action_name", intent.action)
                    putExtra("date", SimpleDateFormat("yyyy/MM/dd").format(Date()))
                    putExtra("time", SimpleDateFormat("HH:mm").format(Date()))
                }
        context.startService(data)
    }
}