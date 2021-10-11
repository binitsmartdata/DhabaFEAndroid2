package com.transport.mall.ui.authentication.login

import android.app.Activity
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentLoginBinding
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.authentication.otpVerification.OtpVerificationFragment
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_login
    override var viewModel: LoginVM
        get() = setUpVM(this, LoginVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentLoginBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.context = getmContext()
        binding.vm = viewModel
        binding.isOwnerLogin = true

        viewModel.observerError()?.observe(this, Observer {
            showSnackBar(binding.root, it.toString(), true)
        })

        viewModel.observerProgress()?.observe(this, Observer {
            if (it) {
                GlobalUtils.hideKeyboard(activity as Activity)
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
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.mobilePrefixObservable.set(binding.ccpCountryCode.selectedCountryCode.toString())
        }
        binding.ccpCountryCode.setCountryForPhoneCode(91)

        binding.btnLogin.setOnClickListener {
            viewModel.doLoginProcess(GenericCallBackTwoParams { output, output2 ->
                when (output) {
                    ApiResult.Status.LOADING -> {
                        showProgressDialog()
                    }
                    ApiResult.Status.ERROR -> {
                        hideProgressDialog()
                        showToastInCenter(output2)
                    }
                    ApiResult.Status.SUCCESS -> {
                        hideProgressDialog()
                        activity?.finish()
                    }
                }
            })
        }
        binding.btnLoginOwner.setOnClickListener {
            viewModel.ownerLogin(GenericCallBackTwoParams { output, output2 ->
                when (output) {
                    ApiResult.Status.LOADING -> {
                        showProgressDialog()
                    }
                    ApiResult.Status.ERROR -> {
                        hideProgressDialog()
                        showToastInCenter(output2)
                    }
                    ApiResult.Status.SUCCESS -> {
                        hideProgressDialog()
                        openFragmentReplaceNoAnim(
                            R.id.authContainer,
                            OtpVerificationFragment(viewModel.userModel),
                            "",
                            true
                        )
                    }
                }
            })
        }

        binding.btnLoginToggle.setOnClickListener {
            binding.isOwnerLogin = !binding.isOwnerLogin!!
        }

        binding.btnSignup.setOnClickListener {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                SignUpFragment(),
                "",
                true
            )
        }
    }
}