package com.example.redrockhalftermwork.jetpack

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskConverter{
    val gson = Gson()

    @TypeConverter
    fun strToTask(str:String):Tasks.Task{
        val type = object : TypeToken<Tasks.Task>(){}.type
        return gson.fromJson(str,type)
    }

    @TypeConverter
    fun taskToStr(task:Tasks.Task):String{
        return gson.toJson(task)
    }

}