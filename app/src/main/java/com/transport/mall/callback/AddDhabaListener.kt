package com.transport.mall.callback

import com.transport.mall.model.DhabaModelMain

interface AddDhabaListener {
    fun showNextScreen()
    fun getDhabaId(): String
    fun getDhabaModelMain(): DhabaModelMain
}