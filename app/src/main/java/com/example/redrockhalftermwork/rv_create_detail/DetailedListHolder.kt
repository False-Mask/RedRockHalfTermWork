package com.example.redrockhalftermwork.rv_create_detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.redrockhalftermwork.jetpack.Tasks

abstract class DetailedListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setBindView(
        position: Tasks,
        position1: Int
    )
}