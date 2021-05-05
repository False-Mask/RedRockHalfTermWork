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
import com.example.redrockhalftermwork.jetpack.TasksDao
import com.example.redrockhalftermwork.rv_create_detail.DetailedListAdapter
import kotlinx.android.synthetic.main.activity_create_detailed_list.*
import kotlinx.android.synthetic.main.detailed_list_item.view.*
import kotlinx.android.synthetic.main.diaglog_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private val job = Job()
private var sqlThread = CoroutineScope(job)

private const val TAG = "CreateDetailedList"
class DetailedList : AppCompatActivity() {
    private var view: View? = null
    //数据库中获取
    private var all:MutableList<Tasks> = mutableListOf()

    private var mId:Long = 0

    //recycler中data
    private lateinit var tasksDao:TasksDao
    private lateinit var tasks:Tasks
    private lateinit var detailedListAdapter:DetailedListAdapter


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
        val task = Tasks.Task()
        createTheDialogAndGetTheContent(task)
    }

    private fun createTheDialogAndGetTheContent(task: Tasks.Task) {
        //将当前时间戳赋值
        task.timePills = System.currentTimeMillis()
        //确定content
        val dialogView = layoutInflater.inflate(R.layout.diaglog_view,null)
        dialogView.apply {
            textView2?.text = "创建任务"
            edit_text.hint = "请输入任务名称"
        }
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        //点击事件
        dialogView.negative_button?.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.positive_button.setOnClickListener {
            //设置content
            task.content = dialogView.edit_text?.text.toString()

            dialog.dismiss()

            //跳转到下一个dialog
            createTheDialogAndGetTheDate(task)
        }

        dialog.show()

    }

    //发送获取Date
    private fun createTheDialogAndGetTheDate(task: Tasks.Task) {
        //初始化时间参数
        val  c = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        //发送一个DatePickerDialog
        //同时更新一下year-month-dayOfMonth
        DatePickerDialog(this@DetailedList, R.style.Theme_AppCompat_Light_Dialog_Alert,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = "$year-$month-$dayOfMonth"
                task.date = date
                ////////////////////////////////////////////////////////////////
                Toast.makeText(this@DetailedList, date, Toast.LENGTH_SHORT).show()

                //跳转到下一个
                createTheDialogAndGetTheTime(task)
            }
            ,year,month,day)
            .show()
    }

    private fun createTheDialogAndGetTheTime(task: Tasks.Task) {
        val c = Calendar.getInstance()
        //发送获取time
        //初始化时间参数
        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minutes = c.get(Calendar.MINUTE)
        TimePickerDialog(this@DetailedList,R.style.Theme_AppCompat_Light_Dialog_Alert,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val time = "$hourOfDay:$minute"
                task.time = time

                //添加task到tasks中
                tasks.list.add(task)
                //设置adapter
                sqlThread.launch(IO) {
                    //更新
                    tasksDao.updateTasks(tasks)
                }
                /////////////////////////////////////////////////////////////////////
                Toast.makeText(this@DetailedList, time, Toast.LENGTH_SHORT).show()
//                if (tasks.id == 0L){
//                    Toast.makeText(this@DetailedList, "room数据库访问异常", Toast.LENGTH_SHORT).show()
//                }
                Toast.makeText(this, "${tasks.id}  $tasks", Toast.LENGTH_SHORT).show()

                detailedListAdapter.notifyDataSetChanged()
            }
            ,hourOfDay,minutes,false).show()


    }

    //初始化Activity UI
    private fun initView(intExtra: Int, booleanExtra: Boolean) {
        //Dao Tasks Adapter初始化
        initData()
        //判断是否是点击创建按钮进入的该Activity
        if (booleanExtra) sendCreateTasksDialog()
        //分线程执行 获取数据库里面的MutableList<Tasks>
        sqlThread.launch(IO){
            all = tasksDao.getAll()
            for (i in all){
                mId = if (i.id > mId) i.id else mId
            }
            mId++

            if (intExtra >= 1){
                //否者数组越界
                copyToTasks(tasks,all[intExtra - 1])
            }

            withContext(Main){
                //init tile
                if (tasks.name != "")  tasks_name.text = tasks.name
                // init recycler
                showDetailed(tasks)
            }
        }
    }

    private fun initData() {
        //初始化Tasks
        tasks = Tasks()
        //初始化Dao
        tasksDao = MyDataBase.getInstance(this).tasksDao()
        //初始化Adapter
        detailedListAdapter = DetailedListAdapter(tasks)
    }

    private fun copyToTasks(tasks: Tasks, tasks1: Tasks) {
        tasks.name = tasks1.name
        tasks.list.addAll(tasks1.list)
    }

    //把任务列表刷新出来 init RecyclerView
    private fun showDetailed(list: Tasks) {
        initAdapterEvent()
        //初始化adapter layoutManager notifyData...
        rv_detailed_list.adapter = detailedListAdapter
        rv_detailed_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        detailedListAdapter.notifyDataSetChanged()
    }

    private fun initAdapterEvent() {
        detailedListAdapter.setOnClickEvent(object : DetailedListAdapter.Listener{
            override fun onSetDateClicked(
                it: View,
                position1: Int,
                tasks: Tasks
            ) {
                //初始化时间参数
                val  c = Calendar.getInstance()
                val year: Int = c.get(Calendar.YEAR)
                val month: Int = c.get(Calendar.MONTH)
                val day: Int = c.get(Calendar.DAY_OF_MONTH)
                //发送一个DatePickerDialog
                //同时更新一下year-month-dayOfMonth
                DatePickerDialog(this@DetailedList, R.style.Theme_AppCompat_Light_Dialog_Alert,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val date = "$year-$month-$dayOfMonth"
                        tasks.list[position1].date = date
                        sqlThread.launch (IO){
                            tasksDao.updateTasks(tasks)
                        }
                        detailedListAdapter.notifyDataSetChanged()
                        Toast.makeText(this@DetailedList, date, Toast.LENGTH_SHORT).show()
                    }
                    ,year,month,day)
                    .show()
            }

            override fun onSetTimeClicked(
                it: View,
                position1: Int,
                tasks: Tasks
            ) {

                //初始化时间参数
                val  c = Calendar.getInstance()
                val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
                val minutes = c.get(Calendar.MINUTE)

                TimePickerDialog(this@DetailedList,R.style.Theme_AppCompat_Light_Dialog_Alert,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        val time = "$hourOfDay:$minute"

                        tasks.list[position1].time = time
                        sqlThread.launch(IO) {
                            tasksDao.updateTasks(tasks)
                        }
                        detailedListAdapter.notifyDataSetChanged()
                        Toast.makeText(this@DetailedList, time, Toast.LENGTH_SHORT).show()
                    }
                    ,hourOfDay,minutes,false).show()


            }

            override fun onSetNameClicked(
                it: View,
                position1: Int,
                tasks: Tasks
            ) {

            }

            override fun onItemClicked(
                it: View,
                position1: Int,
                tasks: Tasks
            ) {
                val visibility = it.hided_view.visibility
                it.hided_view.apply {
                    if (visibility == View.GONE){
                        this.visibility = View.VISIBLE
                    }else if (visibility == View.VISIBLE){
                        this.visibility = View.GONE
                    }
                }

            }

            override fun onStarClicked(
                it: View,
                position1: Int,
                tasks: Tasks
            ) {
                //
            }

            override fun onDeleteClicked(it: View, position1: Int, tasks: Tasks) {
                tasks.list.removeAt(position1)
                sqlThread.launch (IO){
                    tasksDao.updateTasks(tasks)
                }
                detailedListAdapter.notifyDataSetChanged()
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

        (view as View).negative_button?.setOnClickListener {
            Toast.makeText(this, "Negative", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        (view as View).positive_button.setOnClickListener {
            Toast.makeText(this, "Positive", Toast.LENGTH_SHORT).show()
            //EditText输入的名称信息
            val tasksName = (view as View).edit_text.text.toString()
            //修改TextView
            tasks_name.text = tasksName
            //传入到tasks中
            tasks.apply {
                name = tasksName
                list = mutableListOf()
            }

            //放入数据 给MainActivity
            Bundle().apply {
                val mIntent = Intent()
                putSerializable("task",tasks)
                mIntent.putExtras(this)
                setResult(Activity.RESULT_OK,mIntent)
            }

            sqlThread.launch(IO){
                tasks.id = mId++
                tasksDao.insertTask(tasks)
            }
            dialog.dismiss()
        }

        dialog.show()

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