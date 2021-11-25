package com.transport.mall.ui.customdialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogAddManagerBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DailogAddManager constructor(context: Context, owner: UserModel, dhaba: DhabaModel, callBack: GenericCallBack<UserModel?>) : BaseDialog(context) {

    var binding: DialogAddManagerBinding
    var mobilePrefix = ""

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_add_manager, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dhaba = dhaba

        binding.tvAddExisting.setOnClickListener {
            DialogManagerSelection(context, owner._id, GenericCallBack { selectedUser ->
                GlobalUtils.showProgressDialog(context, context.getString(R.string.assigning_manager))
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(
                            getApiService()?.assignManager(dhaba._id, selectedUser._id)
                        ).collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                    GlobalUtils.showProgressDialog(context)
                                }
                                ApiResult.Status.ERROR -> {
                                    GlobalUtils.showToastInCenter(context, it.message.toString())
                                    GlobalUtils.hideProgressDialog()
                                }
                                ApiResult.Status.SUCCESS -> {
                                    GlobalUtils.hideProgressDialog()
                                    GlobalUtils.showToastInCenter(context, context.getString(R.string.manager_assigned_successfully))
                                    callBack.onResponse(selectedUser)
                                    dismiss()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalUtils.hideProgressDialog()
                        GlobalUtils.showToastInCenter(context, e.toString())
                    }
                }
            }).show()
        }
        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCode.setOnCountryChangeListener {
            mobilePrefix = binding.ccpCountryCode.selectedCountryCode
        }
        binding.ccpCountryCode.setCountryForPhoneCode(91)

        binding.ivClose.setOnClickListener {
            callBack.onResponse(null)
            dismiss()
        }

        binding.btnAssign.setOnClickListener {
            if (binding.edMgrName.text?.toString()?.trim()?.isEmpty()!!) {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.enter_mgr_name))
            } else if (binding.edPhoneNumber.text?.toString()?.trim()?.isEmpty()!! || binding.edPhoneNumber.text?.toString()?.trim()?.length!! < 10) {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.enter_valid_mobile))
            } else if (binding.edMgrEmail.text?.toString()?.trim()?.isEmpty()!! || !GlobalUtils.isValidEmail(binding.edMgrEmail.text?.toString()?.trim()!!)) {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.enter_valid_email))
            } else {
                GlobalUtils.showProgressDialog(context, context.getString(R.string.assigning_manager))
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(
                            getApiService()?.addDhabaManager(
                                binding.edMgrName.text?.toString()?.trim()!!,
                                mobilePrefix,
                                binding.edPhoneNumber.text?.toString()?.trim()!!,
                                binding.edMgrEmail.text?.toString()?.trim()!!,
                                dhaba._id
                            )
                        ).collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                    GlobalUtils.showProgressDialog(context)
                                }
                                ApiResult.Status.ERROR -> {
                                    GlobalUtils.showToastInCenter(context, it.message.toString())
                                    GlobalUtils.hideProgressDialog()
                                }
                                ApiResult.Status.SUCCESS -> {
                                    GlobalUtils.hideProgressDialog()
                                    it.data?.let {
                                        if (it.data != null) {
                                            GlobalUtils.showToastInCenter(context, context.getString(R.string.manager_assigned_successfully))
                                            callBack.onResponse(it.data)
                                            dismiss()
                                        } else {
                                            GlobalUtils.showToastInCenter(context, it.message)
                                        }
                                    } ?: kotlin.run {
                                        GlobalUtils.showToastInCenter(context, it.data.toString())
                                    }
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
    }
}