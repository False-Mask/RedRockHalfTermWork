package com.example.redrockhalftermwork.rv_main.tasks

import android.content.Intent
import android.view.View
import androidx.core.os.persistableBundleOf
import com.example.redrockhalftermwork.rv_main.adapter.Holder
import com.example.redrockhalftermwork.rv_main.adapter.ViewData
import kotlinx.android.synthetic.main.rv_tasks.view.*

class TasksHolder(itemView: View) : Holder(itemView) {
    override fun setBindView(holder: MutableList<ViewData>, position: Int) {
        val data = holder[position] as TasksData
        val tasks = data.tasks
        itemView?.rv_item_task_name.text = tasks.name

        itemView?.setOnClickListener {

        }
    }

}