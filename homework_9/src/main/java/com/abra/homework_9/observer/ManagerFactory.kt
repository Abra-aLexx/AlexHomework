package com.abra.homework_9.observer

class ManagerFactory {
    companion object {
        private val manager = DataManager()
        fun getInstance() = manager
    }
}