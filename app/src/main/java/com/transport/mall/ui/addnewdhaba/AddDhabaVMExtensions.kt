package com.transport.mall.ui.addnewdhaba.step4

import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.DhabaTimingModelParent
import com.transport.mall.model.*
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.AddDhabaVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


fun AddDhabaVM.addDhabaOwner(
    ownerModel: UserModel,
    callBack: GenericCallBack<ApiResponseModel<UserModel>>
) {
    submitForApprovalObservers.progressObserverOwner =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverOwner =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateOwner(
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
            submitForApprovalObservers.progressObserverOwner =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleOwnerApiResponse(
    it: ApiResult<ApiResponseModel<UserModel>>,
    callBack: GenericCallBack<ApiResponseModel<UserModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverOwner =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverOwner =
                SubmitForApprovalObservers.Companion.Status_ERROR
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
            submitForApprovalObservers.progressObserverOwner =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addDhaba(
    dhabaModel: DhabaModel,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    submitForApprovalObservers.progressObserverDhaba =
        SubmitForApprovalObservers.Companion.Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addDhaba(
                    RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                    RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(dhabaModel.informations,"")),
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
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateDhaba(
    dhabaModel: DhabaModel,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    submitForApprovalObservers.progressObserverDhaba =
        SubmitForApprovalObservers.Companion.Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateDhaba(
                    RequestBody.create(MultipartBody.FORM, dhabaModel._id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                    RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(dhabaModel.informations,"")),
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
                    /*RequestBody.create(MultipartBody.FORM, if (isDraft) DhabaModel.SubmitForApprovalObservers.Companion.Status_PENDING else DhabaModel.SubmitForApprovalObservers.Companion.Status_INPROGRESS)*/
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
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleDhabaApiResponse(
    it: ApiResult<ApiResponseModel<DhabaModel>>,
    callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_ERROR
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
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}

fun AddDhabaVM.addDhabaTimeing(
    model: DhabaTimingModelParent,
    callBack: GenericCallBack<Boolean>
) {
    submitForApprovalObservers.progressObserverDhaba =
        SubmitForApprovalObservers.Companion.Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addDhabaTimeing(model)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        submitForApprovalObservers.progressObserverDhaba =
                            SubmitForApprovalObservers.Companion.Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        submitForApprovalObservers.progressObserverDhaba =
                            SubmitForApprovalObservers.Companion.Status_ERROR
                        callBack.onResponse(false)
                    }
                    ApiResult.Status.SUCCESS -> {
                        submitForApprovalObservers.progressObserverDhaba =
                            SubmitForApprovalObservers.Companion.Status_SUCCESS
                        callBack.onResponse(true)
                    }
                }
            }
        } catch (e: Exception) {
            submitForApprovalObservers.progressObserverDhaba =
                SubmitForApprovalObservers.Companion.Status_ERROR
//                showToastInCenter(app!!, getCorrectErrorMessage(e))
            callBack.onResponse(false)
        }
    }
}


fun AddDhabaVM.addFoodAmenities(
    model: FoodAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverFood =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverFood =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateFoodAmenities(
    model: FoodAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverFood =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverFood =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleFoodApiResponse(
    it: ApiResult<ApiResponseModel<FoodAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverFood =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverFood =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<FoodAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            submitForApprovalObservers.progressObserverFood =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addParkingAmenities(
    model: ParkingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverParking =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverParking =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateParkingAmenities(
    model: ParkingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverParking =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverParking =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleParkingApiResponse(
    it: ApiResult<ApiResponseModel<ParkingAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverParking =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverParking =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<ParkingAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            submitForApprovalObservers.progressObserverParking =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addSleepingAmenities(
    model: SleepingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverSleeping =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.enclosed, "")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.open, "")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.hotWater, "")
                    ),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_ERROR
                        try {
                            callBack.onResponse(
                                Gson().fromJson(
                                    it.error?.string(),
                                    ApiResponseModel::class.java
                                ) as ApiResponseModel<SleepingAmenitiesModel>?
                            )
                        } catch (e: Exception) {
                            callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                        }
                    }
                    ApiResult.Status.SUCCESS -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_SUCCESS
                        callBack.onResponse(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            submitForApprovalObservers.progressObserverSleeping =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateSleepingAmenities(
    model: SleepingAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverSleeping =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.enclosed, "")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.open, "")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.hotWater, "")
                    ),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_LOADING
                    }
                    ApiResult.Status.ERROR -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_ERROR
                        try {
                            callBack.onResponse(
                                Gson().fromJson(
                                    it.error?.string(),
                                    ApiResponseModel::class.java
                                ) as ApiResponseModel<SleepingAmenitiesModel>?
                            )
                        } catch (e: Exception) {
                            callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                        }
                    }
                    ApiResult.Status.SUCCESS -> {
                        submitForApprovalObservers.progressObserverSleeping =
                            SubmitForApprovalObservers.Companion.Status_SUCCESS
                        callBack.onResponse(it.data)
                    }
                }
            }
        } catch (e: Exception) {
            submitForApprovalObservers.progressObserverSleeping =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}


fun AddDhabaVM.addWashroomAmenities(
    model: WashroomAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverWashroom =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverWashroom =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updatewashroomAmenities(
    model: WashroomAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverWashroom =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverWashroom =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleWashroomApiResponse(
    it: ApiResult<ApiResponseModel<WashroomAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverWashroom =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverWashroom =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<WashroomAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            //                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
            submitForApprovalObservers.progressObserverWashroom =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addSecurityAmenities(
    model: SecurityAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverSecurity =
        SubmitForApprovalObservers.Companion.Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addSecurityAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.dayGuard.toString(), "0")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.nightGuard.toString(), "0")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.policVerification.toString(), "")
                    ),
                    getMultipartImageFile(
                        GlobalUtils.getNonNullString(model.verificationImg, ""),
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
            submitForApprovalObservers.progressObserverSecurity =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateSecurityAmenities(
    model: SecurityAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverSecurity =
        SubmitForApprovalObservers.Companion.Status_LOADING
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateSecurityAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.dayGuard.toString(), "0")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.nightGuard.toString(), "0")
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        GlobalUtils.getNonNullString(model.policVerification.toString(), "")
                    ),
                    getMultipartImageFile(
                        GlobalUtils.getNonNullString(model.verificationImg, ""),
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
            submitForApprovalObservers.progressObserverSecurity =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleSecurityApiResponse(
    it: ApiResult<ApiResponseModel<SecurityAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverSecurity =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverSecurity =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<SecurityAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            submitForApprovalObservers.progressObserverSecurity =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}

fun AddDhabaVM.addLightAmenities(
    model: LightAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverLight =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverLight =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateLightAmenities(
    model: LightAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverLight =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverLight =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleLightAmenitiesApiResponse(
    it: ApiResult<ApiResponseModel<LightAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverLight =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverLight =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<LightAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            submitForApprovalObservers.progressObserverLight =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addOtherAmenities(
    model: OtherAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverOther =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverOther =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateOtherAmenities(
    model: OtherAmenitiesModel,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    submitForApprovalObservers.progressObserverOther =
        SubmitForApprovalObservers.Companion.Status_LOADING
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
            submitForApprovalObservers.progressObserverOther =
                SubmitForApprovalObservers.Companion.Status_ERROR
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleOtherAmenitiesApiResponse(
    it: ApiResult<ApiResponseModel<OtherAmenitiesModel>>,
    callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            submitForApprovalObservers.progressObserverOther =
                SubmitForApprovalObservers.Companion.Status_LOADING
        }
        ApiResult.Status.ERROR -> {
            submitForApprovalObservers.progressObserverOther =
                SubmitForApprovalObservers.Companion.Status_ERROR
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<OtherAmenitiesModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            //                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
            submitForApprovalObservers.progressObserverOther =
                SubmitForApprovalObservers.Companion.Status_SUCCESS
            callBack.onResponse(it.data)
        }
    }
}


fun AddDhabaVM.addBankDetail(
    bankModel: BankDetailsModel,
    callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>
) {
    progressObserver.value = true
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.addBankDetail(
                    RequestBody.create(MultipartBody.FORM, bankModel.user_id),
                    RequestBody.create(MultipartBody.FORM, bankModel.bankName),
                    RequestBody.create(MultipartBody.FORM, bankModel.gstNumber),
                    RequestBody.create(MultipartBody.FORM, bankModel.accountNumber),
                    RequestBody.create(MultipartBody.FORM, bankModel.ifscCode),
                    RequestBody.create(MultipartBody.FORM, bankModel.accountName),
                    RequestBody.create(MultipartBody.FORM, bankModel.panNumber),
                    getMultipartImageFile(bankModel.panPhoto, "panPhoto")
                )
            ).collect {
                handleBankApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserver.value = false
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

fun AddDhabaVM.updateBankDetail(
    bankModel: BankDetailsModel,
    callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>
) {
    progressObserver.value = true
    GlobalScope.launch(Dispatchers.Main) {
        try {
            executeApi(
                getApiService()?.updateBankDetail(
                    RequestBody.create(MultipartBody.FORM, bankModel._id),
                    RequestBody.create(MultipartBody.FORM, bankModel.user_id),
                    RequestBody.create(MultipartBody.FORM, bankModel.bankName),
                    RequestBody.create(MultipartBody.FORM, bankModel.gstNumber),
                    RequestBody.create(MultipartBody.FORM, bankModel.accountNumber),
                    RequestBody.create(MultipartBody.FORM, bankModel.ifscCode),
                    RequestBody.create(MultipartBody.FORM, bankModel.accountName),
                    RequestBody.create(MultipartBody.FORM, bankModel.panNumber),
                    getMultipartImageFile(bankModel.panPhoto, "panPhoto")
                )
            ).collect {
                handleBankApiResponse(it, callBack)
            }
        } catch (e: Exception) {
            progressObserver.value = false
            showToastInCenter(app!!, getCorrectErrorMessage(e))
        }
    }
}

private fun AddDhabaVM.handleBankApiResponse(
    it: ApiResult<ApiResponseModel<BankDetailsModel>>,
    callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>
) {
    when (it.status) {
        ApiResult.Status.LOADING -> {
            progressObserver.value = true
        }
        ApiResult.Status.ERROR -> {
            progressObserver.value = false
            try {
                callBack.onResponse(
                    Gson().fromJson(
                        it.error?.string(),
                        ApiResponseModel::class.java
                    ) as ApiResponseModel<BankDetailsModel>?
                )
            } catch (e: Exception) {
                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
            }
        }
        ApiResult.Status.SUCCESS -> {
            progressObserver.value = false
            callBack.onResponse(it.data!!)
        }
    }
}

fun AddDhabaVM.saveOwnerDetails(it: UserModel, callBack: GenericCallBack<UserModel?>) {
    if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
        updateOwner(it) {
            callBack.onResponse(it.data)
            if (it.data == null) {
                showToastInCenter(app, it.message)
            }
        }
    } else {
        addDhabaOwner(it) {
            callBack.onResponse(it.data)
            if (it.data == null) {
                showToastInCenter(app, it.message)
            }
        }
    }
}

fun AddDhabaVM.saveDhabaDetails(dhabaModel: DhabaModel?, callBack: GenericCallBack<DhabaModel?>) {
    dhabaModel?.let { dhaba ->
        if (GlobalUtils.getNonNullString(dhaba._id, "").isNotEmpty()) {
            updateDhaba(dhaba) {
                callBack.onResponse(it.data)
                if (it.data == null) {
                    showToastInCenter(app, it.message)
                }
            }
        } else {
            addDhaba(dhaba) {
                callBack.onResponse(it.data)
                if (it.data == null) {
                    showToastInCenter(app, it.message)
                }
            }

        }
    }
}

fun AddDhabaVM.saveFoodAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.foodAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateFoodAmenities(it) {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            }
        } else {
            addFoodAmenities(it) {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            }
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveParkingAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.parkingAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateParkingAmenities(it) {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            }
        } else {
            addParkingAmenities(it) {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            }
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveSleepingAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.sleepingAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateSleepingAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addSleepingAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveWashroomAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.washroomAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updatewashroomAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addWashroomAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveSecurityAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.securityAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateSecurityAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addSecurityAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveLightAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.lightAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateLightAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addLightAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveOtherAmenities(
    dhabaModelMain: DhabaModelMain,
    callBack: GenericCallBack<Boolean>
) {
    dhabaModelMain.otherAmenitiesModel?.let {
        it.dhaba_id = dhabaModelMain.dhabaModel?._id + ""
        if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
            updateOtherAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addOtherAmenities(it, GenericCallBack {
                if (it.data != null) {
                    callBack.onResponse(true)
                } else {
                    callBack.onResponse(false)
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(true)
    }
}

fun AddDhabaVM.saveBankDetails(
    bankDetailsModel: BankDetailsModel?,
    callBack: GenericCallBack<BankDetailsModel?>
) {
    bankDetailsModel?.let {
        if (it._id.isNotEmpty()) {
            updateBankDetail(it, GenericCallBack {
                callBack.onResponse(it.data)
                if (it.data == null) {
                    showToastInCenter(app, it.message)
                }
            })
        } else {
            addBankDetail(it, GenericCallBack {
                callBack.onResponse(it.data)
                if (it.data == null) {
                    showToastInCenter(app, it.message)
                }
            })
        }
    } ?: kotlin.run {
        callBack.onResponse(null)
    }
}




