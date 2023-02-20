package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

private const val MYTAG = "UploadWorker"

        //this worker class has two constructor parameters ,
// an instance of Context and an instance of WorkerParameters

class UploadWorker(context:Context,params:WorkerParameters): Worker(context,params) {
    companion object{
        const val KEY_WORKER = "key_worker"
    }
    override fun doWork(): Result {  // always returns a Result
        try {
            // codes to execute the deferrable background task
                val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE,0)
            for (i in 0 until count) {
                Log.i(MYTAG, "Uploading $i")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            val outputData = Data.Builder()
                .putString(KEY_WORKER,currentDate)
                .build()


            return Result.success(outputData)
        }catch (throwable:Throwable){
            return Result.failure()
        }

}
}