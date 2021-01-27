package com.abra.homework_7_executor_service.repositories

import android.content.Context
import com.abra.homework_7_executor_service.data.CarInfo
import com.abra.homework_7_executor_service.data.WorkInfo
import com.abra.homework_7_executor_service.database.DataBaseCarInfo
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class DatabaseRepository(private val service: ExecutorService) {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    fun addWork(info: WorkInfo, callbackListener: () -> Unit) {
        service.submit {
            database.getWorkInfoDAO().addWork(info)
            callbackListener.invoke()
        }
    }

    fun updateWorkInfo(info: WorkInfo, callbackListener: () -> Unit) {
        service.submit {
            database.getWorkInfoDAO().update(info)
            callbackListener.invoke()
        }
    }

    fun deleteWork(info: WorkInfo, callbackListener: () -> Unit) {
        service.submit {
            database.getWorkInfoDAO().delete(info)
            callbackListener.invoke()
        }
    }

    fun getAllWorkListForCar(id: Long, callbackListener: (List<WorkInfo>) -> Unit, executor: Executor) {
        service.submit {
            val list = database.getWorkInfoDAO().getAllWorksForCar(id)
            executor.execute {
                callbackListener.invoke(list)
            }
        }
    }

    fun addCar(info: CarInfo, callbackListener: () -> Unit) {
        service.submit {
            database.getCarInfoDAO().add(info)
            callbackListener.invoke()
        }
    }

    fun updateCarInfo(info: CarInfo, callbackListener: () -> Unit) {
        service.submit {
            database.getCarInfoDAO().update(info)
            callbackListener.invoke()
        }
    }

    fun getAllList(callbackListener: (List<CarInfo>) -> Unit, executor: Executor) {
        service.submit {
            val list = database.getCarInfoDAO().getAll()
            /*
            * Додумался до такого переключения на главный поток,
            * не уверен, что правильно, зато работает и нету бага
            * с TextView(NoCarsAdded)
            * */
            executor.execute {
                callbackListener.invoke(list)
            }
        }
    }
}