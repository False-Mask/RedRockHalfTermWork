package com.example.redrockhalftermwork

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrockhalftermwork.jetpack.MyDataBase
import com.example.redrockhalftermwork.jetpack.Tasks
import com.example.redrockhalftermwork.rv_create_detail.DetailedListAdapter
import kotlinx.android.synthetic.main.activity_create_detailed_list.*
import kotlinx.android.synthetic.main.diaglog_view.view.*
import java.util.*
import kotlin.concurrent.thread

private const val TAG = "CreateDetailedList"
class DetailedList : AppCompatActivity() {
    var view: View? = null
    var tasksName:String = ""
    var detailedListAdapter:DetailedListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        setContentView(R.layout.activity_create_detailed_list)
        initView(intent.getIntExtra("position",-1),intent.getBooleanExtra("toastOrNot",false))
        initEvent()
    }

    private fun setStatusBarColor() {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.detailed_list_background_color)
        }else{
            window.statusBarColor = resources.getColor(R.color.detailed_list_background_color)
        }
    }

    private fun initEvent() {
        detailed_list_back_button.setOnClickListener{
            finish()
        }

        create_task_floating_button.setOnClickListener {
            sendCreateTaskDialog()
        }
    }

    private fun sendCreateTaskDialog() {
//        wf
    }

    //初始化Activity UI
    private fun initView(intExtra: Int, booleanExtra: Boolean) {
        //判断是否要Toast
        if (booleanExtra) sendCreateTasksDialog()
        //获取数据库的实例
        val tasksDao = MyDataBase.getInstance(this).tasksDao()
        //分线程执行
        thread {
            val all = tasksDao.getAll()
            if (intExtra == -1){

            }
            else{
                val tasks = all[intExtra - 1]
                runOnUiThread{
                        //init tile
                        if (tasks.name != "")  tasks_name.text = tasks.name
                        // init recycler
                        if (tasks.list.size != 0) showDetailed(tasks)
                    }
            }
        }
    }

    //把任务列表刷新出来 init RecyclerView
    private fun showDetailed(list: Tasks) {
        detailedListAdapter = DetailedListAdapter(list)
        initAdapterEvent()
        rv_detailed_list.adapter = detailedListAdapter
        rv_detailed_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }

    private fun initAdapterEvent() {
        detailedListAdapter?.setOnClickEvent(object : DetailedListAdapter.Listener{
            override fun onSetDateClicked() {
                //初始化时间参数
                val  c = Calendar.getInstance()
                val year: Int = c.get(Calendar.YEAR)
                val month: Int = c.get(Calendar.MONTH)
                val day: Int = c.get(Calendar.DAY_OF_MONTH)
                //发送一个DatePickerDialog
                DatePickerDialog(this@DetailedList, R.style.Theme_AppCompat_Light_Dialog_Alert,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        Toast.makeText(this@DetailedList, "$year-$month-$dayOfMonth", Toast.LENGTH_SHORT).show();
                    }
                    ,year,month,day)
                    .show()
            }

            override fun onSetTimeClicked() {

                //初始化时间参数
                val  c = Calendar.getInstance()
                val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
                val minutes = c.get(Calendar.MINUTE)

                TimePickerDialog(this@DetailedList,R.style.Theme_AppCompat_Light_Dialog_Alert,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        Toast.makeText(this@DetailedList, "hourOFDay :$hourOfDay\n minute:$minute", Toast.LENGTH_SHORT)
                            .show()
                    }
                    ,hourOfDay,minutes,false).show()


            }

            override fun onSetNameClicked() {

            }

        })
    }

    //创建一个Tasks(也就是主界面上的任务集)
    private fun sendCreateTasksDialog() {
        view = layoutInflater.inflate(R.layout.diaglog_view,null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(true)
            .create()
        dialog.show()

        (view as View).negative_button?.setOnClickListener {
            Toast.makeText(this, "Negative", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        (view as View).positive_button.setOnClickListener {
            Toast.makeText(this, "Positive", Toast.LENGTH_SHORT).show()
            tasksName = (view as View).edit_text.text.toString()
            tasks_name.text = tasksName
            val task = Tasks()
            task.apply {
                name = tasksName
                val mutable = mutableListOf<Tasks.Task>()
                mutable.add(Tasks.Task(System.currentTimeMillis(),"1"))
                mutable.add(Tasks.Task(System.currentTimeMillis(),"2"))
                mutable.add(Tasks.Task(System.currentTimeMillis(),"3"))
                list = mutable
            }
            //放入数据
            Bundle().apply {
                val mIntent = Intent()
                putSerializable("task",task)
                mIntent.putExtras(this)
                setResult(Activity.RESULT_OK,mIntent)
            }

            val tasksDao = MyDataBase.getInstance(this).tasksDao()
            thread{
                tasksDao.insertTask(task)
                val all = tasksDao.getAll()
                for (i in all){
                    Log.e(TAG, "toastAlertDialog: name:${i.name} \n id:${i.id}\n list:${i.list}" )
                }
            }
            dialog.dismiss()
        }

//        val dialog = MyDialog(this,R.style.MyDialog).Builder(this)
//            .setEvent(object : MyDialog.ClickListener{
//                override fun onPositiveClicked(editText: EditText) {
//                    Toast.makeText(this@CreateDetailedList, "1212", Toast.LENGTH_SHORT).show()
//                    Log.e("TAG", "onPositiveClicked: " )
//                    tasksName = editText.text.toString()
//                    tasks_name.text = tasksName
//                }
//
//                override fun negativeClicked() {
//                    Toast.makeText(this@CreateDetailedList, "negative", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//            })
//            .create()
//
//        dialog.show()


    }


}