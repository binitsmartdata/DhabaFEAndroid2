package com.transport.mall.ui.authentication.login

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
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
        binding.ccpCountryCode.isClickable = false
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
                        goToHomeScreen()
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

//        applyHintPositionListener(binding.etEmail, binding.emailTil)
//        applyHintPositionListener(binding.edPwd, binding.passwordTil)
    }

    private fun applyHintPositionListener(editText: EditText, til: TextInputLayout) {
        val hint = til.hint
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().toString().isNotEmpty()) {
                    til.hint = ""
                    editText.y = -4f
                } else {
                    til.hint = hint
                    editText.y = 8f
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        editText.y = -4f
        editText.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                view.y = 8f
            } else {
                if (editText.text?.trim()?.isEmpty()!!) {
                    editText.y = -4f
                }
            }
        }
    }
}