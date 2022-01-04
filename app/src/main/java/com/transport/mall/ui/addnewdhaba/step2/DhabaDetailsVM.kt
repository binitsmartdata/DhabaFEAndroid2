package com.transport.mall.ui.addnewdhaba.step2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.DhabaTimingModelParent
import com.transport.mall.model.DhabaModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.AddDhabaVM
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaDetailsVM(application: Application) : AddDhabaVM(application) {
    var progressObserverDraft: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverUpdate: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverTimings: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverDelImg: MutableLiveData<Boolean> = MutableLiveData()

    var dhabaModel: DhabaModel = DhabaModel()

    init {
        app = application
    }

    fun delDhabaImg(imgId: String, callBack: GenericCallBack<Boolean>) {
        progressObserverDelImg.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.delDhabaImg(dhabaModel._id, imgId)
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserverDelImg.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            progressObserverDelImg.value = false
                            callBack.onResponse(false)
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserverDelImg.value = false
                            callBack.onResponse(true)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserverDelImg.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

}