package com.transport.mall.ui.customdialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.DialogReportReviewBinding
import com.transport.mall.model.ReportReasonModel
import com.transport.mall.model.ReviewModel
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DailogReportReview constructor(
    context: Context,
    user: UserModel,
    review: ReviewModel,
    reportReasons: ArrayList<ReportReasonModel>,
    callBack: GenericCallBack<Boolean>
) : BaseDialog(context) {

    var binding: DialogReportReviewBinding
    var mobilePrefix = ""
    var selectedReasonModel: ReportReasonModel? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_report_review, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ivClose.setOnClickListener {
            callBack.onResponse(false)
            dismiss()
        }

        var designationAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1, reportReasons
        )
        binding.edPropertyStatus.setOnClickListener {
            DialogDropdownOptions(
                context,
                context.getString(R.string.select_reason),
                designationAdapter,
                {
                    binding.edPropertyStatus.setText(reportReasons[it].name)
                    selectedReasonModel = reportReasons[it]
                }).show()
        }

        binding.btnAssign.setOnClickListener {
            if (binding.edPropertyStatus.text.toString().trim().isEmpty()) {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.please_select_reason))
            } else  if (binding.edDescription.text.toString().trim().isEmpty()) {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.enter_description))
            } else{
                GlobalUtils.showProgressDialog(context)
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(
                            getApiService()?.reportReview(
                                SharedPrefsHelper.getInstance(context).getUserData().accessToken,
                                SharedPrefsHelper.getInstance(context).getUserData()._id,
                                review._id,
                                selectedReasonModel?._id.toString(),
                                true,
                                binding.edDescription.text.toString()
                            )
                        ).collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                    GlobalUtils.showProgressDialog(context)
                                }
                                ApiResult.Status.ERROR -> {
                                    callBack.onResponse(false)
                                    GlobalUtils.showToastInCenter(context, Gson().fromJson(it.error?.string(), ApiResponseModel::class.java).message)
                                    GlobalUtils.hideProgressDialog()
                                }
                                ApiResult.Status.SUCCESS -> {
                                    GlobalUtils.hideProgressDialog()
                                    it.data?.let {
                                        if (it.data != null) {
                                            GlobalUtils.showToastInCenter(
                                                context,
                                                context.getString(R.string.report_submitted)
                                            )
                                            callBack.onResponse(true)
                                            dismiss()
                                        } else {
                                            GlobalUtils.showToastInCenter(context, it.message)
                                        }
                                    } ?: kotlin.run {
                                        callBack.onResponse(false)
                                        GlobalUtils.showToastInCenter(context, it.data.toString())
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        callBack.onResponse(false)
                        GlobalUtils.hideProgressDialog()
                        GlobalUtils.showToastInCenter(context, e.toString())
                    }
                }
            }
        }
    }
}