package com.abra.homework_7_completable_future.repositories

import android.content.Context
import com.abra.homework_7_completable_future.data.CarInfo
import com.abra.homework_7_completable_future.data.WorkInfo
import com.abra.homework_7_completable_future.database.DataBaseCarInfo
import java.util.concurrent.CompletableFuture

class DatabaseRepository {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    fun addWork(info: WorkInfo) {
        val completableFuture = CompletableFuture.runAsync {
            database.getWorkInfoDAO().addWork(info)
        }
        if (!completableFuture.isDone) {
            completableFuture.get()
        }
    }

    fun updateWorkInfo(info: WorkInfo) {
        val completableFuture = CompletableFuture.runAsync {
            database.getWorkInfoDAO().update(info)
        }
        if (!completableFuture.isDone) {
            completableFuture.get()
        }
    }

    fun deleteWork(info: WorkInfo) {
        val completableFuture = CompletableFuture.runAsync {
            database.getWorkInfoDAO().delete(info)
        }
        if (!completableFuture.isDone) {
            completableFuture.get()
        }
    }

    fun getAllWorkListForCar(id: Long): List<WorkInfo> {
        val completableFuture = CompletableFuture.supplyAsync {
            return@supplyAsync database.getWorkInfoDAO().getAllWorksForCar(id)
        }
        return completableFuture.get()
    }

    fun addCar(info: CarInfo) {
        val completableFuture = CompletableFuture.runAsync {
            database.getCarInfoDAO().add(info)
        }
        if (!completableFuture.isDone) {
            completableFuture.get()
        }
    }

    fun updateCarInfo(info: CarInfo) {
        val completableFuture = CompletableFuture.runAsync {
            database.getCarInfoDAO().update(info)
        }
        if (!completableFuture.isDone) {
            completableFuture.get()
        }
    }

    fun getAllList(): List<CarInfo> {
        val completableFuture = CompletableFuture.supplyAsync {
            return@supplyAsync database.getCarInfoDAO().getAll()
        }
        return completableFuture.get()
    }
}