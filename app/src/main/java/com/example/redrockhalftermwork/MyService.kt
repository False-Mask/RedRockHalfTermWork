package com.example.redrockhalftermwork

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.constraintlayout.solver.widgets.ConstraintWidget.GONE
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.core.app.NotificationCompat


//常量
private const val CHANNEL_ID = "Service Channel"
private const val CHANNEL_NAME = "Service Channel Name"
private const val TAG = "MyService"
private const val NOTIFICATION_ID = 1
private const val FOREGROUND_SERVICE_ID = 2

class MyService : Service() {


    var builder: NotificationCompat.Builder? = null
    var manager: NotificationManager? = null
    var notification: Notification? = null


    //内部Binder‘工具人’
    inner class MyBinder : Binder() {
        fun getService():MyService{
            return this@MyService
        }

        fun cancelNotification() {
            manager?.cancel(NOTIFICATION_ID)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return  MyBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate: " )
        initNotification()

        super.onCreate()
    }


    private fun initNotification() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //进行版本适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //创建通知渠道
            //版本适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME
                    , NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = "这是一个Channel Description"
                manager!!.createNotificationChannel(channel)
            }
            //创建通知
            builder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.shanghao3)
                .setContentText("这是notification demo的主体内容部分")
                .setContentTitle("这是notification demo的标题")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)

            notification = builder?.build()

        }
        startForeground(FOREGROUND_SERVICE_ID,notification)
//        manager!!.notify(NOTIFICATION_ID, notification)
    }

}
