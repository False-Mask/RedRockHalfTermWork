package com.example.redrockhalftermwork.rv_main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.example.redrockhalftermwork.R
import kotlinx.android.synthetic.main.diaglog_view.*

/**
 * 貌似显示上有点问题
 */
@SuppressWarnings
class MyDialog(context: Context, themeResId: Int) : Dialog(context, themeResId){
    var listener:ClickListener? = null
    var view:View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diaglog_view)

        var inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as (LayoutInflater)
        // instantiate the dialog with the custom Theme
        view = inflater.inflate(R.layout.diaglog_view, null);

        positive_button.setOnClickListener {
            listener?.onPositiveClicked(edit_text)
            cancel()
        }

        negative_button.setOnClickListener {
            listener?.negativeClicked()
            cancel()
        }

    }

    inner class Builder(context:Context){
        fun setEvent(listener: ClickListener):Builder{
            this@MyDialog.listener = listener
            return this
        }

        fun create():MyDialog{
            return this@MyDialog
        }
    }

    interface ClickListener{
        fun onPositiveClicked(editText: EditText)
        fun negativeClicked()
    }
}