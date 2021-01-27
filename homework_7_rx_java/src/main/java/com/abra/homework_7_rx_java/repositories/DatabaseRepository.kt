package com.abra.homework_7_rx_java.repositories

import android.content.Context
import com.abra.homework_7_rx_java.data.CarInfo
import com.abra.homework_7_rx_java.data.WorkInfo
import com.abra.homework_7_rx_java.database.DataBaseCarInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class DatabaseRepository {
    companion object {
        private lateinit var database: DataBaseCarInfo
        fun initDatabase(context: Context) {
            database = DataBaseCarInfo.getDataBase(context)
        }
    }

    fun addWork(info: WorkInfo): Completable = Completable.create {
        database.getWorkInfoDAO().addWork(info)
        it.onComplete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateWorkInfo(info: WorkInfo): Completable = Completable.create {
        database.getWorkInfoDAO().update(info)
        it.onComplete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun deleteWork(info: WorkInfo): Completable = Completable.create {
        database.getWorkInfoDAO().delete(info)
        it.onComplete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getAllWorkListForCar(id: Long): Single<List<WorkInfo>> = Single.create<List<WorkInfo>> {
        val list = database.getWorkInfoDAO().getAllWorksForCar(id)
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun addCar(info: CarInfo): Completable = Completable.create {
        database.getCarInfoDAO().add(info)
        it.onComplete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateCarInfo(info: CarInfo): Completable = Completable.create {
        database.getCarInfoDAO().update(info)
        it.onComplete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getAllList(): Single<List<CarInfo>> = Single.create<List<CarInfo>> {
        val list = database.getCarInfoDAO().getAll()
        it.onSuccess(list)
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}