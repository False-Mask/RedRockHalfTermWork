package com.example.redrockhalftermwork.rv_main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_head.view.*

class MultiRvAdapter(var list: MutableList<ViewData>) : RecyclerView.Adapter<Holder>() {

    var listener:Listener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        //获取Holder类
        return ViewHolderFactory.getViewHolder(
            parent,
            viewType
        )
    }

    override fun getItemCount(): Int {
        //获取Item的数目
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //设置item的显示信息
        holder.setBindView(list,position)

        if(position == 0){
            holder.itemView.apply {
                item_header_import.setOnClickListener {
                    listener?.importantItemClicked()
                }

                item_header_my_day.setOnClickListener {
                    listener?.myDayClicked()
                }

                item_header_my_task.setOnClickListener {
                    listener?.myTasksClicked()
                }
            }
        }else{
            holder.itemView.setOnClickListener {
                listener?.onItemClickListener(it,position)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].getType()
    }


    fun setMultiList(list:MutableList<ViewData>){
        this.list=list
    }

    fun setOnItemClickListener(listener: Listener){
        this.listener = listener
    }


    interface Listener{
        fun onItemClickListener(
            position: View,
            position1: Int
        )

        fun myDayClicked()

        fun importantItemClicked()

        fun myTasksClicked()
    }
}