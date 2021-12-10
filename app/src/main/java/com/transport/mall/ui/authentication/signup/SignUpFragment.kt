package com.transport.mall.ui.authentication.login

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
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

        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.mobilePrefixObserver.set(binding.ccpCountryCode.selectedCountryCode)
        }
        binding.ccpCountryCode.setCountryForPhoneCode(91)

        binding.btnLogin.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


        applyHintPositionListener(binding.edName, binding.tilName)
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