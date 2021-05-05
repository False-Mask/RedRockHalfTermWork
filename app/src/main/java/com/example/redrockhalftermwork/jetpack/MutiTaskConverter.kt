package com.example.redrockhalftermwork.jetpack

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MutiTaskConverter {
    val gson = Gson()

    @TypeConverter
    fun strToTask(data_obj:String):MutableList<Tasks.Task>{
        val type = object : TypeToken<MutableList<Tasks.Task>>(){}.type
        return gson.fromJson(data_obj,type)
    }

    @TypeConverter
    fun taskToStr(data_str:MutableList<Tasks.Task>):String{
        return gson.toJson(data_str)
    }
}