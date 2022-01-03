package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.transport.mall.model.AmenityModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.AddDhabaVM
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AmenititesVM(application: Application) : AddDhabaVM(application) {

    init {
        app = application
    }

    fun getAllAmenities(callBack: GenericCallBack<ArrayList<AmenityModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getAllAmenities()).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserver.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            progressObserver.value = false
                            callBack.onResponse(ArrayList())
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserver.value = false
                            callBack.onResponse(it.data?.data?.data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getAllAmenities :", e.toString())
                progressObserver.value = false
            }
        }
    }
}