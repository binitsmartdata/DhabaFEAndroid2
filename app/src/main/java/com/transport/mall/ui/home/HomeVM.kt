package com.transport.mall.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class HomeVM(application: Application) : BaseVM(application) {
    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var app: Application? = null

    init {
        app = application
    }
}