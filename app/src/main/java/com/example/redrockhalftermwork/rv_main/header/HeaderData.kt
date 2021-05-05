package com.example.redrockhalftermwork.rv_main.header

import com.example.redrockhalftermwork.rv_main.adapter.ViewData
import com.example.redrockhalftermwork.rv_main.adapter.ViewHolderFactory

class HeaderData : ViewData{
    override fun getType(): Int {
        return ViewHolderFactory.HEAD
    }

}