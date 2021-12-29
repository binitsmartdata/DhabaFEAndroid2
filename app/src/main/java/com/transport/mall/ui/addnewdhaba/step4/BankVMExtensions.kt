package com.transport.mall.ui.addnewdhaba.step4

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.DhabaTimingModelParent
import com.transport.mall.model.*
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.step1.BankDetailsVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


fun BankDetailsVM.addDhabaOwner(
    ownerModel: UserModel,
    callBack: GenericCallBack<ApiResponseModel<UserModel>>
) {
    progressObserverOwner = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addOwner(
                    RequestBody.create(MultipartBody.FORM, ownerModel._id),
                    RequestBody.create(MultipartBody.FORM, ownerModel.ownerName),
                    RequestBody.create(MultipartBody.FORM, ownerModel.mobilePrefix),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternativeMobilePrefix),
                    RequestBody.create(MultipartBody.FORM, ownerModel.mobile),
                    RequestBody.create(MultipartBody.FORM, ownerModel.email),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.address),
                    RequestBody.create(MultipartBody.FORM, ownerModel.panNumber),
                    RequestBody.create(MultipartBody.FORM, ownerModel.adharCard),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternativeContactperson),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternatePhone),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternateDesignation),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.latitude),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.longitude),
                    getMultipartImageFile(ownerModel.ownerPic, "ownerPic"),
                    getMultipartImageFile(ownerModel.idproofFront, "idproofFront"),
                    getMultipartImageFile(ownerModel.idproofBack, "idproofBack")
                )
            ).collect {
                handleOwnerApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverOwner = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateOwner(
    ownerModel: UserModel,
    callBack: GenericCallBack<ApiResponseModel<UserModel>>
) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateOwner(
                    RequestBody.create(MultipartBody.FORM, ownerModel._id),
                    RequestBody.create(MultipartBody.FORM, ownerModel.ownerName),
                    RequestBody.create(MultipartBody.FORM, ownerModel.mobilePrefix),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternativeMobilePrefix),
                    RequestBody.create(MultipartBody.FORM, ownerModel.mobile),
                    RequestBody.create(MultipartBody.FORM, ownerModel.email),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.address),
                    RequestBody.create(MultipartBody.FORM, ownerModel.panNumber),
                    RequestBody.create(MultipartBody.FORM, ownerModel.adharCard),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternativeContactperson),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternatePhone),
                    RequestBody.create(MultipartBody.FORM, ownerModel.alternateDesignation),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.latitude),
//                        RequestBody.create(MultipartBody.FORM, ownerModel.longitude),
                    getMultipartImageFile(ownerModel.ownerPic, "profileImage"),
                    getMultipartImageFile(ownerModel.idproofFront, "idproofFront"),
                    getMultipartImageFile(ownerModel.idproofBack, "idproofBack")
                )
            ).collect {
                handleOwnerApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverOwner = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleOwnerApiResponse(
    it: ApiResult<ApiResponseModel<UserModel>>,
    callBack: GenericCallBack<ApiResponseModel<UserModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverOwner = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverOwner = Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<UserModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverOwner = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun BankDetailsVM.addDhaba(
    dhabaModel: DhabaModel,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    progressObserverDhaba = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addDhaba(
                    RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.address),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.landmark),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.area),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.highway),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.state),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.city),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.pincode),
                    RequestBody.create(
                        MultipartBody.FORM,
                        dhabaModel.latitude + "," + dhabaModel.longitude
                    ),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.mobile),
                    if (dhabaModel.propertyStatus != null && dhabaModel.propertyStatus.isNotEmpty()) RequestBody.create(
                        MultipartBody.FORM,
                        dhabaModel.propertyStatus
                    ) else null,
                    RequestBody.create(MultipartBody.FORM, DhabaModel.STATUS_PENDING),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.latitude),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.longitude),
                    getMultipartImageFile(dhabaModel.images, "images"),
                    getMultipartImagesList(dhabaModel.imageList, "imageList"),
                    getMultipartVideoFile(dhabaModel.videos, "videos"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ),
                    if (SharedPrefsHelper.getInstance(app!!).getUserData()
                            .isExecutive()
                    ) RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ) else null,
                    RequestBody.create(MultipartBody.FORM, dhabaModel.open247.toString()),
                    RequestBody.create(
                        MultipartBody.FORM,
                        if (SharedPrefsHelper.getInstance(app!!).getUserData()
                                .isExecutive()
                        ) "quality-control" else "executive"
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().role.toString()
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().role.toString()
                    )
                )
            ).collect {
                handleDhabaApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverDhaba = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateDhaba(
    dhabaModel: DhabaModel,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    progressObserverDhaba = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateDhaba(
                    RequestBody.create(MultipartBody.FORM, dhabaModel._id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.address + " "),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.landmark),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.area),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.highway),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.state),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.city),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.pincode),
                    RequestBody.create(
                        MultipartBody.FORM,
                        dhabaModel.latitude + "," + dhabaModel.longitude
                    ),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.mobile),
                    if (dhabaModel.propertyStatus != null && dhabaModel.propertyStatus.isNotEmpty()) RequestBody.create(
                        MultipartBody.FORM,
                        dhabaModel.propertyStatus
                    ) else null,
                    /*RequestBody.create(MultipartBody.FORM, if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS)*/
                    null,
                    RequestBody.create(MultipartBody.FORM, dhabaModel.latitude),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.longitude),
                    getMultipartImageFile(dhabaModel.images, "images"),
                    getMultipartImagesList(dhabaModel.imageList, "imageList"),
                    getMultipartVideoFile(dhabaModel.videos, "videos"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ),
                    if (SharedPrefsHelper.getInstance(app!!).getUserData()
                            .isExecutive()
                    ) RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData()._id
                    ) else null,
                    RequestBody.create(MultipartBody.FORM, dhabaModel.open247.toString()),
                    RequestBody.create(
                        MultipartBody.FORM,
                        if (SharedPrefsHelper.getInstance(app!!).getUserData()
                                .isExecutive()
                        ) "quality-control" else "executive"
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().role.toString()
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().role.toString()
                    )
                )
            ).collect {
                handleDhabaApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverDhaba = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleDhabaApiResponse(
    it: ApiResult<ApiResponseModel<DhabaModel>>,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverDhaba = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverDhaba = Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<DhabaModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverDhaba = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}

fun BankDetailsVM.addDhabaTimeing(
    model: DhabaTimingModelParent,
    callBack: GenericCallBack<Boolean>
) {
    progressObserverDhabaTiming = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addDhabaTimeing(model)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverDhabaTiming = Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverDhabaTiming = Status_ERROR
                        callBack.onResponse(false)
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserverDhabaTiming = Status_SUCCESS
                        callBack.onResponse(true)
                    }
                }
            }
        } catch (e: Exception) {
            progressObserverDhabaTiming = Status_ERROR
//                showToastInCenter(app!!, getCorrectErrorMessage(e))
            callBack.onResponse(false)
        }
    }
}


fun BankDetailsVM.addFoodAmenities(
    model: FoodAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    progressObserverFood = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addFoodAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.foodAt100, "")
                    ),
                    RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                    RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.food, "")
                    ),
                    RequestBody.create(MultipartBody.FORM, model.foodLisence),
                    getMultipartImageFile(model.foodLisenceFile, "foodLisenceFile"),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleFoodApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverFood = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateFoodAmenities(
    model: FoodAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    progressObserverFood = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateFoodAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.foodAt100, "")
                    ),
                    RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                    RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.food, "")
                    ),
                    RequestBody.create(MultipartBody.FORM, model.foodLisence),
                    getMultipartImageFile(model.foodLisenceFile, "foodLisenceFile"),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleFoodApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverFood = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleFoodApiResponse(
    it: ApiResult<ApiResponseModel<FoodAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverFood = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverFood = Status_ERROR
            callBack.onResponse(
                ApiResponseModel(
                    0,
                    it.message!!,
                    null
                )
            )
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverFood = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun BankDetailsVM.addParkingAmenities(
    model: ParkingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    progressObserverParking = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addParkingAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.concreteParking),
                    RequestBody.create(MultipartBody.FORM, model.concreteParkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.flatHardParking),
                    RequestBody.create(MultipartBody.FORM, model.flatHardParkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.kachaFlatParking),
                    RequestBody.create(MultipartBody.FORM, model.parkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.parkingStatus),
                    RequestBody.create(MultipartBody.FORM, model.parkingAvailable.toString()),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleParkingApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverParking = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateParkingAmenities(
    model: ParkingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    progressObserverParking = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateParkingAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.concreteParking),
                    RequestBody.create(MultipartBody.FORM, model.concreteParkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.flatHardParking),
                    RequestBody.create(MultipartBody.FORM, model.flatHardParkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.kachaFlatParking),
                    RequestBody.create(MultipartBody.FORM, model.parkingSpace),
                    RequestBody.create(MultipartBody.FORM, model.parkingStatus),
                    RequestBody.create(MultipartBody.FORM, model.parkingAvailable.toString()),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleParkingApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverParking = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleParkingApiResponse(
    it: ApiResult<ApiResponseModel<ParkingAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverParking = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverParking = Status_ERROR
            callBack.onResponse(
                ApiResponseModel(
                    0,
                    it.message!!,
                    null
                )
            )
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverParking = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun BankDetailsVM.addSleepingAmenities(
    model: SleepingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>
) {
    progressObserverSleeping = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addSleepingAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.sleeping),
                    RequestBody.create(MultipartBody.FORM, model.noOfBeds),
                    RequestBody.create(MultipartBody.FORM, model.fan),
                    RequestBody.create(MultipartBody.FORM, model.cooler),
                    RequestBody.create(MultipartBody.FORM, model.enclosed),
                    RequestBody.create(MultipartBody.FORM, model.open),
                    RequestBody.create(MultipartBody.FORM, model.hotWater),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverSleeping = Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverSleeping = Status_ERROR
                        callBack.onResponse(
                            ApiResponseModel(
                                0,
                                it.message!!,
                                null
                            )
                        )
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserverSleeping = Status_SUCCESS
                        callBack.onResponse(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            progressObserverSleeping = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateSleepingAmenities(
    model: SleepingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>
) {
    progressObserverSleeping = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateSleepingAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.sleeping),
                    RequestBody.create(MultipartBody.FORM, model.noOfBeds),
                    RequestBody.create(MultipartBody.FORM, model.fan),
                    RequestBody.create(MultipartBody.FORM, model.cooler),
                    RequestBody.create(MultipartBody.FORM, model.enclosed),
                    RequestBody.create(MultipartBody.FORM, model.open),
                    RequestBody.create(MultipartBody.FORM, model.hotWater),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverSleeping = Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverSleeping = Status_ERROR
                        callBack.onResponse(
                            ApiResponseModel(
                                0,
                                it.message!!,
                                null
                            )
                        )
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserverSleeping = Status_SUCCESS
                        callBack.onResponse(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            progressObserverSleeping = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}


fun BankDetailsVM.addWashroomAmenities(
    model: WashroomAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    progressObserverWashroom = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addWashroomAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.washroomStatus),
                    RequestBody.create(MultipartBody.FORM, model.water),
                    RequestBody.create(MultipartBody.FORM, model.cleaner),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleWashroomApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverWashroom = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updatewashroomAmenities(
    model: WashroomAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    progressObserverWashroom = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updatewashroomAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.washroomStatus),
                    RequestBody.create(MultipartBody.FORM, model.water),
                    RequestBody.create(MultipartBody.FORM, model.cleaner),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
                handleWashroomApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverWashroom = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleWashroomApiResponse(
    it: ApiResult<ApiResponseModel<WashroomAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverWashroom = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverWashroom = Status_ERROR
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
            progressObserverWashroom = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun BankDetailsVM.addSecurityAmenities(
    model: SecurityAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    progressObserverSecurity = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addSecurityAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.dayGuard.toString()),
                    RequestBody.create(MultipartBody.FORM, model.nightGuard.toString()),
                    RequestBody.create(MultipartBody.FORM, model.policVerification.toString()),
                    getMultipartImageFile(
                        model.verificationImg,
                        "verificationImg"
                    ),
                    RequestBody.create(MultipartBody.FORM, model.indoorCamera.toString()),
                    getMultipartImagesList(
                        model.indoorCameraImage,
                        "indoorCameraImage"
                    ),
                    RequestBody.create(MultipartBody.FORM, model.outdoorCamera.toString()),
                    getMultipartImagesList(
                        model.outdoorCameraImage,
                        "outdoorCameraImage"
                    )
                )
            ).collect {
                handleSecurityApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverSecurity = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateSecurityAmenities(
    model: SecurityAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    progressObserverSecurity = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateSecurityAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.dayGuard.toString()),
                    RequestBody.create(MultipartBody.FORM, model.nightGuard.toString()),
                    RequestBody.create(MultipartBody.FORM, model.policVerification.toString()),
                    getMultipartImageFile(
                        model.verificationImg,
                        "verificationImg"
                    ),
                    RequestBody.create(MultipartBody.FORM, model.indoorCamera.toString()),
                    getMultipartImagesList(
                        model.indoorCameraImage,
                        "indoorCameraImage"
                    ),
                    RequestBody.create(MultipartBody.FORM, model.outdoorCamera.toString()),
                    getMultipartImagesList(
                        model.outdoorCameraImage,
                        "outdoorCameraImage"
                    )
                )
            ).collect {
                handleSecurityApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverSecurity = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleSecurityApiResponse(
    it: ApiResult<ApiResponseModel<SecurityAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverSecurity = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverSecurity = Status_ERROR
            callBack.onResponse(
                ApiResponseModel(
                    0,
                    it.message!!,
                    null
                )
            )
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverSecurity = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}

fun BankDetailsVM.addLightAmenities(
    model: LightAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    progressObserverLight = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addLightAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.towerLight.toString()),
                    getMultipartImageFile(model.towerLightImage, "towerLightImage"),
                    RequestBody.create(MultipartBody.FORM, model.bulbLight.toString()),
                    getMultipartImageFile(model.bulbLightImage, "bulbLightImage"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        model.electricityBackup.toString()
                    )
                )
            ).collect {
                handleLightAmenitiesApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverLight = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateLightAmenities(
    model: LightAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    progressObserverLight = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateLightAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.towerLight.toString()),
                    getMultipartImageFile(model.towerLightImage, "towerLightImage"),
                    RequestBody.create(MultipartBody.FORM, model.bulbLight.toString()),
                    getMultipartImageFile(model.bulbLightImage, "bulbLightImage"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        model.electricityBackup.toString()
                    )
                )
            ).collect {
                handleLightAmenitiesApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverLight = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleLightAmenitiesApiResponse(
    it: ApiResult<ApiResponseModel<LightAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverLight = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverLight = Status_ERROR
            callBack.onResponse(
                ApiResponseModel(
                    0,
                    it.message!!,
                    null
                )
            )
        }
        ApiResult.Status.SUCCESS -> {
            progressObserverLight = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun BankDetailsVM.addOtherAmenities(
    model: OtherAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    progressObserverOther = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addOtherAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.dailyutilityshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.utilityShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.barber.toString()),
                    RequestBody.create(MultipartBody.FORM, model.barberShopType.toString()),
                    getMultipartImagesList(
                        model.barberImages,
                        "barberImages"
                    )
                )
            ).collect {
                handleOtherAmenitiesApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverOther = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun BankDetailsVM.updateOtherAmenities(
    model: OtherAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    progressObserverOther = Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateOtherAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.dailyutilityshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.utilityShopType.toString()),
                    RequestBody.create(MultipartBody.FORM, model.barber.toString()),
                    RequestBody.create(MultipartBody.FORM, model.barberShopType.toString()),
                    getMultipartImagesList(
                        model.barberImages,
                        "barberImages"
                    )
                )
            ).collect {
                handleOtherAmenitiesApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserverOther = Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun BankDetailsVM.handleOtherAmenitiesApiResponse(
    it: ApiResult<ApiResponseModel<OtherAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserverOther = Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            progressObserverOther = Status_ERROR
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
            progressObserverOther = Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}