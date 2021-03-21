package com.abra.homework_11.observer

import com.abra.homework_11.json_structure.LogData

/**
 * Решил вместо лисенеры сделать через старый добрый
 * Observer и вроде получилось.(Что-то решение через лисенер мне
 * не особо понравилось)
 * */
class LogsManager {
    companion object {
        private val manager = LogsManager()
        fun getInstance() = manager
    }

    private val listData = Data()
    private val subscribersList = mutableListOf<Observer>()

    fun subscribe(observer: Observer) {
        subscribersList.add(observer)
    }

    fun unsubscribe(observer: Observer) {
        subscribersList.remove(observer)
    }

    fun setData(list: MutableList<LogData>) {
        listData.list = list
        notifyAllObservers(listData.list)
    }

    private fun notifyAllObservers(list: MutableList<LogData>) {
        if (subscribersList.size != 0) {
            subscribersList.forEach {
                it.update(list)
            }
        }
    }
}