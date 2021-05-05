package com.example.redrockhalftermwork.jetpack

import android.app.Application
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

private const val TAG = "MainActivityLifecycleObserver"
class MainActivityLifecycleObserver : LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
////        响铃
//        val mp = MediaPlayer()
//        mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))//这里我用的通知声音，还有其他的，大家可以点进去看
//        mp.prepare()
//        mp.start()
//        //循环播放
//        mp.setLooping(true)
//
//
////        震动
////        取得震动服务的句柄
////        取得震动服务的句柄
//        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        vibrator.vibrate(longArrayOf(1000, 1000), 0)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    //前台进入后台
    fun onStop(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    //后台进入前台
    fun onResume(){

    }
}