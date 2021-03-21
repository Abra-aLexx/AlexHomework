package com.abra.homework_10.observer

import com.abra.homework_10.presentation.database.CityData

/**
 * Пригодился мне паттерн Observer, так как нужно было добавить город в сисок
 * в DialogFragment, а onResume почему-то не отрабатывает, когда диалог перекрывает фрагмент,
 * либо я что-то неправильно сделал, но главное, что проблему я решил использовав этот паттерн.
 * */
class DataManager {
    private val listData = Data()
    private val subscribers = mutableListOf<Observer>()
    fun subscribe(observer: Observer) {
        subscribers.add(observer)
    }

    fun unsubscribe(observer: Observer) {
        subscribers.remove(observer)
    }

    fun setList(list: List<CityData>) {
        listData.list = list
        notifyAllSubscribers()
    }

    private fun notifyAllSubscribers() {
        subscribers.forEach {
            it.update(listData.list)
        }
    }
}

/**
 * Класс хранит уже обновленный список
 * */
class Data {
    var list = listOf<CityData>()
}