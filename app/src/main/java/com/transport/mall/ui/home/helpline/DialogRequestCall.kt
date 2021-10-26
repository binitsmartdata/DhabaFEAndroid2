package com.transport.mall.ui.home.helpline

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogRequestForCallBinding
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.customdialogs.BaseDialog
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DialogRequestCall constructor(
    context: Context
) : BaseDialog(context) {

    var binding: DialogRequestForCallBinding
    var prefix = "91"

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_request_for_call, null, false
        )
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ccpCountryCode.setCountryForPhoneCode(91)
        binding.ccpCountryCode.setOnCountryChangeListener {
            prefix = binding.ccpCountryCode.selectedCountryCode
        }

        binding.btnCallMe.setOnClickListener {
            if (binding.edPhoneNumber.text.toString().trim().length < 10) {
                GlobalUtils.shakeThisView(context, binding.editlayout)
            } else {
                GlobalUtils.showProgressDialog(context)
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(getApiService()?.callRequest(SharedPrefsHelper.getInstance(context).getUserData().accessToken, prefix, binding.edPhoneNumber.text.toString().trim())).collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                    GlobalUtils.showProgressDialog(context)
                                }
                                ApiResult.Status.ERROR -> {
                                    GlobalUtils.hideProgressDialog()
                                    GlobalUtils.showToastInCenter(context, it.message.toString())
                                }
                                ApiResult.Status.SUCCESS -> {
                                    GlobalUtils.hideProgressDialog()
                                    GlobalUtils.showInfoDialog(context, context.getString(R.string.call_request_submitted), {
                                        dismiss()
                                    })
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalUtils.showToastInCenter(context, e.toString())
                    }
                }
            }
        }

    }
}