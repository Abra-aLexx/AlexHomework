package com.abra.homework_10.presentation.database

import android.app.Application
import com.abra.homework_10.data.repositories.DatabaseRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initDatabase(this)
    }
}