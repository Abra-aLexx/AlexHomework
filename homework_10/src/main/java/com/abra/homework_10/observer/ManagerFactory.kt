package com.abra.homework_10.observer

class ManagerFactory {
    companion object {
        private val manager = DataManager()
        fun getInstance() = manager
    }
}