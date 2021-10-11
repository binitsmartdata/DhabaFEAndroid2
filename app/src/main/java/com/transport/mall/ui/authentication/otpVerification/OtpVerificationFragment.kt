package com.transport.mall.ui.authentication.otpVerification

import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentOtpVerificationBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OtpVerificationFragment(val userModel: UserModel) : BaseFragment<FragmentOtpVerificationBinding, OtpVerificationVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_otp_verification
    override var viewModel: OtpVerificationVM
        get() = setUpVM(this, OtpVerificationVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOtpVerificationBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.vm = viewModel
        viewModel.userModel = userModel
        var prefix = if (userModel.mobilePrefix.isNotEmpty()) userModel.mobilePrefix else ""
        binding.tvOtpSentTo.text = "$prefix ${userModel.mobile}"
        binding.btnLoginOwner.setOnClickListener {
            if (binding.edOtp.text.toString().isNotEmpty() && binding.edOtp.text.toString().length == 6) {
                viewModel.otp = binding.edOtp.text.toString()
                viewModel.checkOtp(GenericCallBack {
                    it.data?.let {
                        SharedPrefsHelper.getInstance(getmContext()).setUserData(it)
                        activity?.finish()
                        goToHomeScreen()
                    } ?: kotlin.run {
                        showToastInCenter(it.message.toString())
                    }
                })
            } else {
                showToastInCenter(getString(R.string.enter_valid_otp))
            }
        }

        binding.btnResentOtp.setOnClickListener {
            viewModel.resendOtp(GenericCallBack { response ->
                response.data?.let {
                    showToastInCenter(getString(R.string.otp_sent))
                } ?: kotlin.run {
                    showToastInCenter(response.message.toString())
                }
            })
        }
    }

    override fun initListeners() {
        viewModel.observerProgress()?.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.verifying))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.resendOtpObserver?.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
    }
}