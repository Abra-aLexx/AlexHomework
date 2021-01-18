package com.abra.homework_7_executor_service.repositories

import android.content.Context
import com.abra.homework_7_executor_service.data.CarInfo
import com.abra.homework_7_executor_service.data.WorkInfo
import com.abra.homework_7_executor_service.database.DataBaseCarInfo
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseRepository {
    companion object {
        private val service: ExecutorService = Executors.newSingleThreadExecutor()
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
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
            service.submit {
                database.getWorkInfoDAO().update(info)
            }
        }

        fun deleteWork(info: WorkInfo) {
            service.submit {
                database.getWorkInfoDAO().delete(info)
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
            service.submit {
                database.getCarInfoDAO().update(info)
            }
        }

        fun getAllList(): List<CarInfo> {
            val future = service.submit<List<CarInfo>> {
                return@submit database.getCarInfoDAO().getAll()
            }
            return future.get()
        }

        fun shutdown() {
            service.shutdown()
        }
    }
}