package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.AddDhabaVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class BankDetailsVM(application: Application) : AddDhabaVM(application) {
    var progressObserverSubmit: MutableLiveData<Boolean> = MutableLiveData()
    var bankModel = BankDetailsModel()
    var dhabaModel = DhabaModel()
    var blockingMonths: ObservableField<String> = ObservableField()

    init {
        app = application

        blockingMonths.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                blockingMonths.get()
                    ?.let { dhabaModel.blockMonth = GlobalUtils.getNonNullString(it, "0").toInt() }
            }
        })
    }

}