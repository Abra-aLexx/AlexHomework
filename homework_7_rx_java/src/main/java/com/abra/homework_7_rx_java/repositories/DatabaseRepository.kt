package com.abra.homework_7_rx_java.repositories

import android.content.Context
import com.abra.homework_7_rx_java.data.CarInfo
import com.abra.homework_7_rx_java.data.WorkInfo
import com.abra.homework_7_rx_java.database.DataBaseCarInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class DatabaseRepository {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    fun addWork(info: WorkInfo) {
        Single.create<WorkInfo> {
            database.getWorkInfoDAO().addWork(info)
            it.onSuccess(info)
        }.subscribeOn(Schedulers.io())
                .blockingSubscribe()
    }

    fun updateWorkInfo(info: WorkInfo) {
        Single.create<WorkInfo> {
            database.getWorkInfoDAO().update(info)
            it.onSuccess(info)
        }.subscribeOn(Schedulers.io())
                .blockingSubscribe()
    }

    fun deleteWork(info: WorkInfo) {
        Single.create<WorkInfo> {
            database.getWorkInfoDAO().delete(info)
            it.onSuccess(info)
        }.subscribeOn(Schedulers.io())
                .blockingSubscribe()
    }

    fun getAllWorkListForCar(id: Long): Single<List<WorkInfo>> = Single.create<List<WorkInfo>> {
        val list = database.getWorkInfoDAO().getAllWorksForCar(id)
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun addCar(info: CarInfo) {
        Single.create<CarInfo> {
            database.getCarInfoDAO().add(info)
            it.onSuccess(info)
        }.subscribeOn(Schedulers.io())
                .blockingSubscribe()
    }

    fun updateCarInfo(info: CarInfo) {
        Single.create<CarInfo> {
            database.getCarInfoDAO().update(info)
            it.onSuccess(info)
        }.subscribeOn(Schedulers.io())
                .blockingSubscribe()
    }

    fun getAllList(): Single<List<CarInfo>> = Single.create<List<CarInfo>> {
        val list = database.getCarInfoDAO().getAll()
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}