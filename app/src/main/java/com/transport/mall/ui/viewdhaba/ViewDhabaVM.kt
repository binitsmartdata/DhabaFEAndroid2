package com.transport.mall.ui.viewdhaba

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ViewDhabaVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var mDhabaModelMain = DhabaModelMain()

    init {
        app = application
    }
}