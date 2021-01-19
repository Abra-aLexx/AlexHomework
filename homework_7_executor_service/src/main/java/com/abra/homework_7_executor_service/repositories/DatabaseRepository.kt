package com.abra.homework_7_executor_service.repositories

import android.content.Context
import com.abra.homework_7_executor_service.data.CarInfo
import com.abra.homework_7_executor_service.data.WorkInfo
import com.abra.homework_7_executor_service.database.DataBaseCarInfo
import java.util.concurrent.ExecutorService

class DatabaseRepository(private val service: ExecutorService) {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    fun addWork(info: WorkInfo) {
        val future = service.submit {
            database.getWorkInfoDAO().addWork(info)
        }
        if (!future.isDone) {
            future.get()
        }
    }

    fun updateWorkInfo(info: WorkInfo) {
        val future = service.submit {
            database.getWorkInfoDAO().update(info)
        }
        if (!future.isDone) {
            future.get()
        }
    }

    fun deleteWork(info: WorkInfo) {
        val future = service.submit {
            database.getWorkInfoDAO().delete(info)
        }
        if (!future.isDone) {
            future.get()
        }
    }

    fun getAllWorkListForCar(id: Long): List<WorkInfo> {
        val future = service.submit<List<WorkInfo>> {
            return@submit database.getWorkInfoDAO().getAllWorksForCar(id)
        }
        return future.get()
    }

    fun addCar(info: CarInfo) {
        val future = service.submit {
            database.getCarInfoDAO().add(info)
        }
        if (!future.isDone) {
            future.get()
        }
    }

    fun updateCarInfo(info: CarInfo) {
        val future = service.submit {
            database.getCarInfoDAO().update(info)
        }
        if (!future.isDone) {
            future.get()
        }
    }

    fun getAllList(): List<CarInfo> {
        val future = service.submit<List<CarInfo>> {
            return@submit database.getCarInfoDAO().getAll()
        }
        return future.get()
    }
}