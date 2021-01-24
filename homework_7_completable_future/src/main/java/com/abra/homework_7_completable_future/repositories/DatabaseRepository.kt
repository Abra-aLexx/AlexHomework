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
    fun addWork(info: WorkInfo): CompletableFuture<Void> = CompletableFuture.runAsync {
        database.getWorkInfoDAO().addWork(info)
    }

    fun updateWorkInfo(info: WorkInfo): CompletableFuture<Void> = CompletableFuture.runAsync {
        database.getWorkInfoDAO().update(info)
    }

    fun deleteWork(info: WorkInfo): CompletableFuture<Void> = CompletableFuture.runAsync {
            database.getWorkInfoDAO().delete(info)
        }

    fun getAllWorkListForCar(id: Long): CompletableFuture<List<WorkInfo>> = CompletableFuture.supplyAsync {
            return@supplyAsync database.getWorkInfoDAO().getAllWorksForCar(id)
        }

    fun addCar(info: CarInfo): CompletableFuture<Void> = CompletableFuture.runAsync {
            database.getCarInfoDAO().add(info)
        }

    fun updateCarInfo(info: CarInfo): CompletableFuture<Void> = CompletableFuture.runAsync {
            database.getCarInfoDAO().update(info)
        }

    fun getAllList(): CompletableFuture<List<CarInfo>> = CompletableFuture.supplyAsync {
        return@supplyAsync database.getCarInfoDAO().getAll()
    }
}