package com.transport.mall.ui.authentication.login

import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentLoginBinding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.repository.networkoperator.ApiResult

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
        binding.vm = viewModel
        viewModel.observerError()?.observe(this, Observer {
            showSnackBar(binding.root, it.toString(), true)
        })

        viewModel.observerProgress()?.observe(this, Observer {

        })

        viewModel.toggleProgressDialog()?.observe(this, Observer {
            if (it) {
                GlobalUtils.showProgressDialog(activity)
            } else {
                GlobalUtils.hideProgressDialog()
            }
        })
        viewModel.emailObservable.set("ashish@mail.com")
        viewModel.passwordObservable.set("123456")
    }

    override fun initListeners() {
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
    }
}