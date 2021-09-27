package com.transport.mall.callback

import com.transport.mall.model.DhabaModelMain

interface AddDhabaListener {
    fun showNextScreen()
    fun saveAsDraft()
    fun getDhabaId(): String
    fun getDhabaModelMain(): DhabaModelMain
    fun isUpdate(): Boolean
    fun viewOnly(): Boolean
}