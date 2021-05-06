package com.example.redrockhalftermwork

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrockhalftermwork.jetpack.MainActivityLifecycleObserver
import com.example.redrockhalftermwork.jetpack.MyDataBase
import com.example.redrockhalftermwork.jetpack.Tasks
import com.example.redrockhalftermwork.rv_main.adapter.MultiRvAdapter
import com.example.redrockhalftermwork.rv_main.adapter.ViewData
import com.example.redrockhalftermwork.rv_main.adapter.ViewHolderFactory
import com.example.redrockhalftermwork.rv_main.header.HeaderData
import com.example.redrockhalftermwork.rv_main.tasks.TasksData
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

private const val TAG = "MainActivity"
private const val CREATE_NEW_DETAINED_LIST = 1
class MainActivity : AppCompatActivity() {

    private val con = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected: ")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected: " )
            binder = service as MyService.MyBinder
            this@MainActivity.service = binder?.getService()
        }
    }

    private var binder: MyService.MyBinder? = null
    private var service:MyService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(MainActivityLifecycleObserver())


        //响铃
//        val mp = MediaPlayer()
//        mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))//这里我用的通知声音，还有其他的，大家可以点进去看
//        mp.prepare()
//        mp.start()
//        //循环播放
//        mp.setLooping(true)


        //震动
        //取得震动服务的句柄
        //取得震动服务的句柄
//        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        vibrator.vibrate(longArrayOf(1000, 1000), 0)
        iniRecyclerView()

        create_new_detailed_list.setOnClickListener {
            val mIntent = Intent(this, DetailedList::class.java)
            mIntent.putExtra("toastOrNot",true)
            startActivityForResult(mIntent, CREATE_NEW_DETAINED_LIST)
        }


    }

    private fun setStatusBarColor() {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.main_activity_status_bar_color)
        }else{
            window.statusBarColor = resources.getColor(R.color.main_activity_status_bar_color)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CREATE_NEW_DETAINED_LIST->{
                if (resultCode == Activity.RESULT_OK){
                    val task = data?.extras?.getSerializable("task") as Tasks
                    rvDataList.add(TasksData(task))
                    multiAdapter.notifyDataSetChanged()
                }
            }
            else->{
                //...
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    lateinit var multiAdapter:MultiRvAdapter
    private var rvDataList = mutableListOf<ViewData>()

    private fun iniRecyclerView() {
        //配置viewData
        rvDataList.add(HeaderData())
        multiAdapter = MultiRvAdapter(rvDataList)

        //设置adapter
        multiAdapter.setOnItemClickListener(object : MultiRvAdapter.Listener{
            override fun onItemClickListener(
                position: View,
                position1: Int
            ) {
                when(multiAdapter.getItemViewType(position1)){
                    ViewHolderFactory.HEAD->{
                        //...
                    }
                    ViewHolderFactory.TASKS->{
                        //
                        val mIntent = Intent(this@MainActivity,DetailedList::class.java)
                        mIntent.putExtra("position",position1)
                        startActivityForResult(mIntent,position1)
                    }
                }
            }

            override fun myDayClicked() {
                Toast.makeText(this@MainActivity, "MyDay", Toast.LENGTH_SHORT).show()
                val mIntent = Intent(this@MainActivity,DetailedList::class.java)
                mIntent.putExtra("is_my_day",true)
//                startActivity(mIntent)
            }

            override fun importantItemClicked() {
                Toast.makeText(this@MainActivity, "Important", Toast.LENGTH_SHORT).show()

            }

            override fun myTasksClicked() {
                Toast.makeText(this@MainActivity, "MyTask", Toast.LENGTH_SHORT).show()

            }

        })
        rv.adapter = multiAdapter
        rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        thread {
            val tasksDao = MyDataBase.getInstance(this).tasksDao()
            val all = tasksDao.getAll()
            for(i in all){
                rvDataList.add(TasksData(i))
            }
            runOnUiThread{
                multiAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binder?.cancelNotification()
    }

    override fun onStop() {
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        bindService(intent, con, Context.BIND_AUTO_CREATE)
        super.onStop()
    }

    override fun onDestroy() {
        unbindService(con)
        super.onDestroy()
    }
}