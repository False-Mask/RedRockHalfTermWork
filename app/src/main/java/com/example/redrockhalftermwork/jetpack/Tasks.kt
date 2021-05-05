package com.example.redrockhalftermwork.jetpack

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "tasksTable")
data class Tasks(
    var name:String = "",
    var list:MutableList<Task> = mutableListOf()
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0


    data class Task(
        //创建时间
        var timePills:Long = 0,
        //内容(也就是名称)
        var content:String = "",
        //日期
        var date:String = "",
        //时间
        var time:String  = ""
    ):Serializable
}