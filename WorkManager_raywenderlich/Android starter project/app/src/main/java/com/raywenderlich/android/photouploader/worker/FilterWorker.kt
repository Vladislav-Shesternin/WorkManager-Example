package com.raywenderlich.android.photouploader.worker

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.raywenderlich.android.photouploader.ImageUtils

private const val IMAGE_PATH_PREFIX = "IMAGE_PATH_"
private const val TAG = "FilterWorker"

const val KEY_IMAGE_URI = "IMAGE_URI"
const val KEY_IMAGE_INDEX = "IMAGE_INDEX"

class FilterWorker : Worker() {

    override fun doWork(): WorkerResult {
        return try {
            Thread.sleep(3000)
            Log.i(TAG, "START")

            val imageUriString = inputData.getString(KEY_IMAGE_URI, null)
            val imageIndex = inputData.getInt(KEY_IMAGE_INDEX, 0)

            val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    Uri.parse(imageUriString)
            )

            val filteredBitmap = ImageUtils.applySepiaFilter(bitmap)
            val filteredImageUri = ImageUtils.writeBitmapToFile(applicationContext, filteredBitmap)

            outputData = Data.Builder().putString(
                    IMAGE_PATH_PREFIX + imageIndex,
                    filteredImageUri.toString()
            ).build()

            Log.i(TAG, "Success!")
            WorkerResult.SUCCESS

        } catch (e: Throwable) {
            Log.i(TAG, e.message)
            WorkerResult.FAILURE
        }
    }
}