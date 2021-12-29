package com.transport.mall.ui.addnewdhaba

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.SubmitForApprovalObservers
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2019-12-06.
 */
open class AddDhabaVM(application: Application) : BaseVM(application) {
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var app: Application? = null

    var mDhabaModelMain = DhabaModelMain()
    var submitForApprovalObservers = SubmitForApprovalObservers()

    init {
        app = application
    }
}