package com.abra.homework_11.json_structure

import com.google.gson.annotations.SerializedName

data class Logs(
        @SerializedName("logs_list")
        val logsList: MutableList<LogData>
)

data class LogData(
        @SerializedName("action_name")
        val actionName: String,
        val date: String,
        val time: String
)