package com.example.redrockhalftermwork.rv_create_detail

import android.view.View
import com.example.redrockhalftermwork.R
import com.example.redrockhalftermwork.jetpack.Tasks
import kotlinx.android.synthetic.main.detailed_list_item.view.*

class DetailedListHolderImp(itemView: View) : DetailedListHolder(itemView) {
    override fun setBindView(
        tasks: Tasks,
        position1: Int
    ) {
        itemView.rv_detailed_content.text = tasks.list[position1].content
        itemView.rv_detailed_image.setImageResource(R.drawable.ic_baseline_star_border_24)
        itemView.rv_detailed_position.text = (position1 + 1).toString()
    }

}