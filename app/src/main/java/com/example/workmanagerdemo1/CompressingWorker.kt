package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

private const val MYTAG = "UploadWorker"

class CompressingWorker(context:Context,params:WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        try {

            for (i in 0..300) {
                Log.i(MYTAG, "Compressing $i")
            }

            return Result.success()
        }catch (throwable:Throwable){
            return Result.failure()
        }

    }
}