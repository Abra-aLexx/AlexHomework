package com.abra.homework_8_2.functions

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import com.abra.homework_8_2.R
import java.io.File
import java.io.FileOutputStream

fun saveImage(photo: Bitmap, iv: ImageView, carPictureDirectory: File): String {
    val path = "photo_${System.currentTimeMillis()}.jpg"
    val pathToPicture = "${carPictureDirectory.path}/${path}"
    val file = File(carPictureDirectory, path)
    file.createNewFile()
    val stream = FileOutputStream(file)
    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    iv.setImageBitmap(photo)
    stream.flush()
    stream.close()
    return pathToPicture
}

fun setImageStatus(status: String, resources: Resources, iv: ImageView, tv: TextView) {
    when (status) {
        resources.getString(R.string.pending) -> {
            iv.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
            tv.setTextColor(resources.getColor(R.color.work_status_pending))
        }
        resources.getString(R.string.in_progress_lowe_case) -> {
            iv.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
            tv.setTextColor(resources.getColor(R.color.work_status_in_progress))
        }
        resources.getString(R.string.completed_in_lower_case) -> {
            iv.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
            tv.setTextColor(resources.getColor(R.color.work_status_completed))
        }
    }
}

fun createDirectory(context: Context): File? {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        val carPictureDirectory = File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/CarPictures")
        if (!carPictureDirectory.exists()) {
            carPictureDirectory.mkdir()
        }
        return carPictureDirectory
    }
    return null
}