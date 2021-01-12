package com.abra.homework_6_2

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class ImageLoader {
    companion object{
        fun loadAllPhotos(activity: Activity): ArrayList<Uri>{
            val photoUriList = arrayListOf<Uri>()
            val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor?
            val columnIndexID: Int
            val projection = arrayOf(MediaStore.Images.Media._ID)
            var imageId: Long
            cursor = activity.contentResolver.query(uriExternal, projection, null, null, null)
            if (cursor != null) {
                columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    imageId = cursor.getLong(columnIndexID)
                    val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                    photoUriList.add(uriImage)
                }
                cursor.close()
            }
            return photoUriList
        }
    }
}