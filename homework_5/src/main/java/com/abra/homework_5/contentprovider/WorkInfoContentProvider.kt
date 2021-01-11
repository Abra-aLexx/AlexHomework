package com.abra.homework_5.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.abra.homework_5.database.DataBaseCarInfo

class WorkInfoContentProvider: ContentProvider() {
    private var database: DataBaseCarInfo? = null
    companion object{
        private const val AUTHORITY = "com.abra.homework_5.contentprovider.WorkInfoContentProvider"
        private const val URI_USER_CODE = 1
        private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY,"works_info", URI_USER_CODE)
        }
    }
    override fun onCreate(): Boolean {
        context?.run {
            database = DataBaseCarInfo.getDataBase(this.applicationContext)
        }
        return false
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {

        var cursor: Cursor? = null
        when(uriMatcher.match(p0)){
            URI_USER_CODE->{
                cursor = database?.getWorkInfoDAO()?.selectAll()
            }
        }
        return cursor
    }

    override fun getType(p0: Uri): String? {
        return "object/*"
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}