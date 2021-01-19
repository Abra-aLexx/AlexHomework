package com.abra.homework_7_executor_service.services

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseService {
    companion object {
        private val SERVICE = Executors.newSingleThreadExecutor()
        fun getInstance(): ExecutorService = SERVICE
    }
}