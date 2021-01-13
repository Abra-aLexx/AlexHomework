package com.abra.homework_6_1

import android.app.Activity
import android.net.Uri

class WorksLoader {
    companion object {

        private const val URI_PATH = "content://com.abra.homework_5.contentprovider.WorkInfoContentProvider/works_info"

        fun loadAllWorks(activity: Activity): ArrayList<WorkInfo> {
            val workList = arrayListOf<WorkInfo>()
            val cursor = activity.contentResolver.query(Uri.parse(URI_PATH), null, null, null, null)
            cursor?.run {
                while (moveToNext()) {
                    workList.add(WorkInfo(getString(getColumnIndex("date")),
                            getString(getColumnIndex("workName")),
                            getString(getColumnIndex("description")),
                            getString(getColumnIndex("cost")),
                            getString(getColumnIndex("status"))))
                }
                cursor.close()
            }
            return workList
        }
    }
}