package com.transport.mall.ui.authentication.login

import android.app.Activity
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentSignUpBinding
import com.transport.mall.ui.authentication.signup.SignUpVM
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_sign_up
    override var viewModel: SignUpVM
        get() = setUpVM(this, SignUpVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentSignUpBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.context = getmContext()
        binding.vm = viewModel

        viewModel.observerError()?.observe(this, Observer {
            showSnackBar(binding.root, it.toString(), true)
        })

        viewModel.observerProgress()?.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.please_wait))
            } else {
                hideProgressDialog()
            }
        })

        viewModel.progressObserverCityStates?.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.fetching_states_cities_highways))
            } else {
                hideProgressDialog()
            }
        })

        viewModel.toggleProgressDialog()?.observe(this, Observer {
            if (it) {
                GlobalUtils.showProgressDialog(activity)
            } else {
                GlobalUtils.hideProgressDialog()
            }
        })
    }

    override fun initListeners() {
        binding.btnSignUp.setOnClickListener {
            viewModel.signUp(GenericCallBack {
                if (it.data != null) {
                    showToastInCenter(getString(R.string.signup_success))
                    activity?.supportFragmentManager?.popBackStack()
                } else {
                    showToastInCenter(it.message)
                }
            })
        }

        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.mobilePrefixObserver.set(binding.ccpCountryCode.selectedCountryCode)
        }
        binding.ccpCountryCode.setCountryForPhoneCode(91)

        binding.btnLogin.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}