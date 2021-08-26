package com.transport.mall.ui.authentication.otpVerification

import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentOtpVerificationBinding
import com.transport.mall.utils.base.BaseFragment

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class OtpVerificationFragment : BaseFragment<FragmentOtpVerificationBinding, OtpVerificationVM>() {
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
        viewModel.observerError()?.observe(this, Observer {
            showSnackBar(binding.root, it.toString(), true)
        })

        viewModel.observerProgress()?.observe(this, Observer {

        })
    }

    override fun initListeners() {

    }
}