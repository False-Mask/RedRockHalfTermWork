package com.example.redrockhalftermwork.rv_create_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redrockhalftermwork.R
import com.example.redrockhalftermwork.jetpack.Tasks

class DetailedListAdapter(var dataList:Tasks): RecyclerView.Adapter<DetailedListHolder>() {

    override fun getItemCount(): Int {
        return dataList.list.size
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: DetailedListHolder, position: Int) {
        holder.setBindView(dataList,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedListHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.detailed_list_item, parent, false)
        return DetailedListHolderImp(inflate)
    }

}