package com.example.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanagerdemo1.UploadWorker.Companion.KEY_WORKER
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object{
        const val KEY_COUNT_VALUE = "key_count"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val button = findViewById<Button>(R.id.button)


        button.setOnClickListener {
           // setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }
    private fun setOneTimeWorkRequest(){
        val textView = findViewById<TextView>(R.id.textView)
        // codes to tell the work manager to perform the task
        val workManager =  WorkManager.getInstance(applicationContext)
        // create a data object and set that data object to request object
        val data:Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE,125)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadOneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()
        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()
        val downloadingRequest = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()
        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(downloadingRequest)
        parallelWorks.add(filteringRequest)

//this is sequential chaining
            workManager
                .beginWith(parallelWorks)
                .then(compressingRequest)
                .then(uploadOneTimeWorkRequest)
                .enqueue()


        //codes to observe the work info
        workManager.getWorkInfoByIdLiveData(uploadOneTimeWorkRequest.id)
            .observe(this@MainActivity, {
                textView.text = it.state.name
                if(it.state.isFinished){}
                val data = it.outputData
                //we can get the string message using the key
                val message = data.getString(KEY_WORKER)
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()

            })



    }
    private fun setPeriodicWorkRequest(){
     val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES)
         .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}