package com.example.redrockhalftermwork.rv_main.tasks

import com.example.redrockhalftermwork.jetpack.Tasks
import com.example.redrockhalftermwork.rv_main.adapter.ViewData
import com.example.redrockhalftermwork.rv_main.adapter.ViewHolderFactory

class TasksData(var tasks:Tasks) : ViewData{
    override fun getType(): Int {
        return ViewHolderFactory.TASKS
    }
}