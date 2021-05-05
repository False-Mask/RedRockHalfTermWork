package com.example.redrockhalftermwork.jetpack

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val TASKS_DATABASE_NAME = "Tasks_Data.db"

@TypeConverters(MutiTaskConverter::class)
@Database(version = 1, entities = [Tasks::class],exportSchema = false)
abstract class MyDataBase : RoomDatabase(){

    abstract fun tasksDao():TasksDao
    companion object{
        private var instance:MyDataBase?=null

        @Synchronized
        fun getInstance(context: Context):MyDataBase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                MyDataBase::class.java, TASKS_DATABASE_NAME)
                .build().apply {
                    instance=this
                }
        }
    }
}