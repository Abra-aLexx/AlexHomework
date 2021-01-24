package com.abra.homework_7_coroutines.repositories

import android.content.Context
import com.abra.homework_7_coroutines.data.CarInfo
import com.abra.homework_7_coroutines.data.WorkInfo
import com.abra.homework_7_coroutines.database.DataBaseCarInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabaseRepository(private val scope: CoroutineScope) {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    suspend fun addWork(info: WorkInfo) {
        withContext(scope.coroutineContext + Dispatchers.IO) { database.getWorkInfoDAO().addWork(info) }
    }

    suspend fun updateWorkInfo(info: WorkInfo) {
        withContext(scope.coroutineContext + Dispatchers.IO) { database.getWorkInfoDAO().update(info) }
    }

    suspend fun deleteWork(info: WorkInfo) {
        withContext(scope.coroutineContext + Dispatchers.IO) { database.getWorkInfoDAO().delete(info) }
    }

    suspend fun getAllWorkListForCar(id: Long) = withContext(scope.coroutineContext + Dispatchers.IO) { database.getWorkInfoDAO().getAllWorksForCar(id) }

    suspend fun addCar(info: CarInfo) {
        withContext(scope.coroutineContext + Dispatchers.IO) { database.getCarInfoDAO().add(info) }
    }

    suspend fun updateCarInfo(info: CarInfo) {
        withContext(scope.coroutineContext + Dispatchers.IO) { database.getCarInfoDAO().update(info) }
    }

    suspend fun getAllList() = withContext(scope.coroutineContext + Dispatchers.IO) { database.getCarInfoDAO().getAll() }

}