package com.transport.mall.ui.authentication.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.healthiex.naha.repository.networkoperator.Result
import com.transport.mall.R
import com.transport.mall.databinding.FragmentLoginBinding
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GlobalUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Vishal Sharma on 2019-12-06.
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

    }

    override fun initListeners() {
        binding.btnLogin.setOnClickListener {

            startHomeActivity()
        /*
            when (binding.etEmail.text.toString().isNotEmpty() && binding.edPwd.text.toString()
                .isNotEmpty()) {
                true -> {
                    startHomeActivity()

                    lifecycleScope.launch {
                        viewModel.getItemById2().collect {
                            when (it.status) {
                                Result.Status.LOADING -> {
                                    showProgressDialog()
                                }
                                Result.Status.ERROR -> {
                                    hideProgressDialog()
                                    Log.i("ERROR ::", it.message)
                                }
                                Result.Status.SUCCESS -> {
                                    hideProgressDialog()
                                    startHomeActivity()

                                    Log.i("RESPONSE ::", it.data?.string())
                                }
                            }
                        }
                    }

                }
                else -> {
                    when {
                        binding.etEmail.text.toString()
                            .isEmpty() -> viewModel.errorResponse?.value =
                            "Email field cannot be empty"
                        binding.edPwd.text.toString().isEmpty() -> viewModel.errorResponse?.value =
                            "Password field cannot be empty"
                    }
                }
            }
*/

        }
    }

    private fun startHomeActivity() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }
}