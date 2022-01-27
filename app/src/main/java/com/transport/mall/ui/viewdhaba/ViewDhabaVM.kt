package com.transport.mall.ui.viewdhaba

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDocsListModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.ReportReasonModel
import com.transport.mall.model.ReviewModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ViewDhabaVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverReasons: MutableLiveData<Boolean> = MutableLiveData()

    var mDhabaModelMain = DhabaModelMain()

    var reviewList = ArrayList<ReviewModel>()

    var dhabaId: String = ""
    var reviewModel = ReviewModel()
    var page = 1
    var count = 10

    init {
        app = application
    }

    fun addReview(review_id: String, comment: String, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addReview(
                        dhabaId,
                        SharedPrefsHelper.getInstance(app as Context).getUserData()._id,
                        reviewModel.review,
                        reviewModel.rating.toString()
                    )
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserver.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            callBack.onResponse(false)
                            progressObserver.value = false
                            showToastInCenter(app as Context, it.message.toString())
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserver.value = false
                            if (it.data?.status != 200) {
                                showToastInCenter(app as Context, it.data?.message.toString())
                            }
                            callBack.onResponse(true)
                        }
                    }
                }
            } catch (e: Exception) {
                callBack.onResponse(false)
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun addReviewReply(review_id: String, comment: String, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addReviewReply(
                        review_id,
                        SharedPrefsHelper.getInstance(app as Context).getUserData()._id,
                        dhabaId,
                        comment
                    )
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserver.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            callBack.onResponse(false)
                            progressObserver.value = false
                            showToastInCenter(app as Context, it.message.toString())
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserver.value = false
                            if (it.data?.status != 200) {
                                showToastInCenter(app as Context, it.data?.message.toString())
                            }
                            callBack.onResponse(true)
                        }
                    }
                }
            } catch (e: Exception) {
                callBack.onResponse(false)
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun getDhabaReviewsById(callBack: GenericCallBack<ApiResponseModel<InternalDocsListModel<ArrayList<ReviewModel>>>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.getDhabaReviewsById(
                        dhabaId,
                        count.toString(),
                        page.toString(),
                        "DESC"
                    )
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserver.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            callBack.onResponse(null)
                            progressObserver.value = false
                            showToastInCenter(app as Context, it.message.toString())
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserver.value = false
                            if (it.data?.status != 200) {
                                showToastInCenter(app as Context, it.data?.message.toString())
                            }
                            callBack.onResponse(it.data)
                        }
                    }
                }
            } catch (e: Exception) {
                callBack.onResponse(null)
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun getAllReasons(callBack: GenericCallBack<ArrayList<ReportReasonModel>>) {
        progressObserverReasons.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getAllReasons(SharedPrefsHelper.getInstance(app?.applicationContext!!).getUserData().accessToken)).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserverReasons.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            callBack.onResponse(null)
                            progressObserverReasons.value = false
                            showToastInCenter(app as Context, it.message.toString())
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserverReasons.value = false
                            if (it.data?.status != 200) {
                                showToastInCenter(app as Context, it.data?.message.toString())
                            }
                            callBack.onResponse(it.data?.data?.data)
                        }
                    }
                }
            } catch (e: Exception) {
                callBack.onResponse(null)
                progressObserverReasons.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }
}