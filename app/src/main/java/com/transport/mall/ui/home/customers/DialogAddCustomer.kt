package com.transport.mall.ui.home.customers

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.DialogAddCustomerBinding
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.customdialogs.BaseDialog
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DialogAddCustomer constructor(context: Context) : BaseDialog(context) {

    var binding: DialogAddCustomerBinding
    var dhabaId = "0"

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_add_customer, null, false
        )
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window!!.setGravity(Gravity.BOTTOM)

        binding.bSendInvitation.setOnClickListener {
            onButtonClick()
        }
    }

    private fun onButtonClick() {
        if (binding.etCustomerName.text?.trim().toString().isEmpty()) {
            binding.etCustomerName.error = context.getString(R.string.please_enter_customer_name)
            return
        }
        if (binding.edPhoneNumber.text?.trim().toString().isEmpty()) {
            binding.edPhoneNumber.error = context.getString(R.string.please_enter_customer_phone_number)
            return
        }

        GlobalUtils.showProgressDialog(context, context.getString(R.string.sending_invite))
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addCustomer_Invites(
                        dhabaId,
                        SharedPrefsHelper.getInstance(context).getUserData()._id,
                        SharedPrefsHelper.getInstance(context).getUserData()._id,
                        "+91",
                        binding.edPhoneNumber.text.toString(),
                        binding.etCustomerName.text.toString()
                    )
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            GlobalUtils.showProgressDialog(context)
                        }
                        ApiResult.Status.ERROR -> {
                            var message = (Gson().fromJson(
                                it.error?.string(),
                                ApiResponseModel::class.java
                            ) as ApiResponseModel<UserModel>?)?.message
                            GlobalUtils.showToastInCenter(context, message.toString())
                            GlobalUtils.hideProgressDialog()
                        }
                        ApiResult.Status.SUCCESS -> {
                            GlobalUtils.hideProgressDialog()
                            DialogSuccess(
                                context, "An invitation link sent to " +
                                        binding.etCustomerName.text?.trim()
                            ).show()
                            dismiss()
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalUtils.hideProgressDialog()
                GlobalUtils.showToastInCenter(context, e.toString())
            }
        }
    }
}