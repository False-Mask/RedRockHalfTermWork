package com.example.redrockhalftermwork.rv_main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.redrockhalftermwork.R
import com.example.redrockhalftermwork.rv_main.header.HeaderHolder
import com.example.redrockhalftermwork.rv_main.tasks.TasksHolder

class ViewHolderFactory {
    companion object {
        const val HEAD = 1
        const val TASKS = 2
        fun getViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return when(viewType){
                HEAD -> HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_head,parent,false))
                TASKS -> TasksHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_tasks,parent,false))
                else -> { TODO()}
            }
        }
    }
}