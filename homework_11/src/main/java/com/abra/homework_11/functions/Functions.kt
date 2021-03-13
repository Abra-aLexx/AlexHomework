package com.abra.homework_11.functions

import android.content.Context
import android.os.Environment
import java.io.File

fun createFile(context: Context): File? {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        val logsFile = File("${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}/logs.json")
        if (!logsFile.exists()) {
            logsFile.createNewFile()
        }
        return logsFile
    }
    return null
}