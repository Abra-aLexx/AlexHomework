package com.abra.homework_11.repository

import android.content.Context
import com.abra.homework_11.functions.createFile
import com.abra.homework_11.json_structure.Logs
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogsRepository(private val context: Context,
                     private val coroutineScope: CoroutineScope) {
    private val file = createFile(context)
    private val jsonPretty = GsonBuilder().setPrettyPrinting().create()
    suspend fun getLogs(): Logs =
            withContext(coroutineScope.coroutineContext + Dispatchers.IO) {
                val stringJson = file?.readText(Charsets.UTF_8) ?: ""
                if (stringJson.isNotEmpty()) {
                    return@withContext jsonPretty.fromJson(stringJson, Logs::class.java)
                } else {
                    return@withContext Logs(mutableListOf())
                }
            }
}