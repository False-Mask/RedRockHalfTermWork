package com.example.redrockhalftermwork.rv_create_detail

import android.view.View
import com.example.redrockhalftermwork.R
import com.example.redrockhalftermwork.jetpack.Tasks
import kotlinx.android.synthetic.main.detailed_list_item.view.*

class DetailedListHolderImp(itemView: View) : DetailedListHolder(itemView) {
    override fun setBindView(
        tasks: Tasks,
        position1: Int,
        listener: DetailedListAdapter.Listener
    ) {

        itemView.apply {
            rv_detailed_content.text = tasks.list[position1].content
            rv_detailed_image.setImageResource(R.drawable.ic_baseline_star_border_24)
            rv_detailed_position.text = (position1 + 1).toString()
            val mDate = tasks.list[position1].date
            if (mDate!=""){
                date.text = mDate
            }

            val mTime = tasks.list[position1].time
            if (mTime != ""){
                time.text = mTime
            }

            set_date.setOnClickListener {
                listener.onSetDateClicked(it,position1,tasks)
            }

            set_name.setOnClickListener {
                listener.onSetNameClicked(it,position1,tasks)
            }

            set_time.setOnClickListener {
                listener.onSetTimeClicked(it,position1,tasks)
            }

            item_change_visibility.setOnClickListener{
                listener.onItemClicked(it,position1,tasks)
            }

            rv_detailed_image.setOnClickListener {
                listener.onStarClicked(it,position1,tasks)
            }

            delete_task.setOnClickListener{
                listener.onDeleteClicked(it,position1,tasks)
            }
        }

    }

}