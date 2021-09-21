package com.transport.mall.utils.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.transport.mall.repository.networkcheck.ConnectivityReceiver
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BaseApplication : Application(), LifecycleObserver,
    ConnectivityReceiver.ConnectivityReceiverListener {
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.e("AppApplication", "isConnected : $isConnected")
    }

    val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    override fun onCreate() {
        super.onCreate()
        // startConnectivityService()
        // scheduleJob()
    }

    private fun startConnectivityService() {
//        val startServiceIntent = Intent(applicationContext, NetworkSchedulerService::class.java)
//        startService(startServiceIntent)
    }

    private fun stopConnectivityService() {
        // stopService(Intent(applicationContext, NetworkSchedulerService::class.java))
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        Log.d("AppController", "Foreground")
        startConnectivityService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        Log.d("AppController", "Background")
        stopConnectivityService()
    }

    private fun scheduleJob() {
        /* val myJob = JobInfo.Builder(0, ComponentName(this, NetworkSchedulerService::class.java))
             .setRequiresCharging(true)
             .setMinimumLatency(1000)
             .setOverrideDeadline(2000)
             .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
             .setPersisted(true)
             .build()

         val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
         jobScheduler.schedule(myJob)*/
    }

}