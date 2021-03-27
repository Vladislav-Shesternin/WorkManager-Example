package com.raywenderlich.android.photouploader.worker

import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.raywenderlich.android.photouploader.ImageUtils

private const val TAG = "CompressWorker"

private const val KEY_IMAGE_PATH = "IMAGE_PATH"
private const val KEY_ZIP_PATH = "ZIP_PATH"

class CompressWorker : Worker() {

    override fun doWork(): WorkerResult {
        return try {
            Thread.sleep(3000)
            Log.i(TAG, "Compressing files!")

            val imagePaths = inputData.keyValueMap
                    .filter { it.key.startsWith(KEY_IMAGE_PATH) }
                    .map { it.value as String }

            val zipFile = ImageUtils.createZipFile(
                    applicationContext,
                    imagePaths.toTypedArray()
            )

            outputData = Data.Builder()
                    .putString(KEY_ZIP_PATH, zipFile.path)
                    .build()

            Log.i(TAG, "Success!")
            WorkerResult.SUCCESS

        } catch (e: Throwable) {
            Log.i(TAG, e.message)
            WorkerResult.FAILURE
        }
    }
}