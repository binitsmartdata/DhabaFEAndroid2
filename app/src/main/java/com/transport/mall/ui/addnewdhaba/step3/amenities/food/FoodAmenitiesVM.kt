package com.transport.mall.ui.addnewdhaba.step3.amenities.food

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.FoodAmenitiesModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class FoodAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var model = FoodAmenitiesModel()
    var dhabaModelMain = DhabaModelMain()

    init {
        app = application
    }

    fun addFoodAmenities(callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            uploadFoodAmenities().collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value =
                            true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(
                            ApiResponseModel(
                                0,
                                it.message!!,
                                null
                            )
                        )
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data)
                    }
                }
            }
        }
    }

    suspend fun uploadFoodAmenities(): Flow<ApiResult<ApiResponseModel<FoodAmenitiesModel>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.addFoodAmenities(
                            RequestBody.create(MultipartBody.FORM, "1"),
                            RequestBody.create(MultipartBody.FORM, "1"),
                            RequestBody.create(MultipartBody.FORM, "6137443bb5828a682d08ecf1"),
                            RequestBody.create(MultipartBody.FORM, model.foodAt100),
                            RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                            RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                            RequestBody.create(MultipartBody.FORM, model.food),
                            RequestBody.create(MultipartBody.FORM, model.foodLisence),
                            getMultipartImageFile(model.foodLisenceFile, "foodLisenceFile"),
                            getMultipartImagesList(model.images, "images")
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}