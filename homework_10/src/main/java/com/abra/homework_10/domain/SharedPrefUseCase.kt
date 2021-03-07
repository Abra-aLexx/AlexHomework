package com.abra.homework_10.domain

import android.content.Context
import com.abra.homework_10.data.repositories.SharedPrefRepository

class SharedPrefUseCase(context: Context?) {
    private val repository = SharedPrefRepository(context)
    fun writeId(checkedCityId: Int) {
        repository.writeId(checkedCityId)
    }

    fun getId() = repository.getId()
}