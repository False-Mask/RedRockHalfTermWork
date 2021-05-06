package com.example.redrockhalftermwork.jetpack

import androidx.room.*

@Dao
interface TasksDao {
    @Insert
    fun insertTask(task:Tasks)

    @Delete
    fun deleteTask(task: Tasks)

    @Query("Select * from tasksTable")
    fun getAll():MutableList<Tasks>

    @Update
    fun updateTasks(task:Tasks)


}