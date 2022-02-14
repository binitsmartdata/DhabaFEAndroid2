package com.transport.mall.ui.home.helpline

import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.FragmentRequestForCallBinding
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class RequestForCallFragment : BaseFragment<FragmentRequestForCallBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_request_for_call
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentRequestForCallBinding
        get() = setUpBinding()
        set(value) {}

    var userModel: UserModel? = UserModel()
    var prefix = "91"

    override fun bindData() {
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        binding.edPhoneNumber.setText(SharedPrefsHelper.getInstance(getmContext()).getUserData().mobile)
        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCode.setCountryForPhoneCode(91)
        binding.ccpCountryCode.setOnCountryChangeListener {
            prefix = binding.ccpCountryCode.selectedCountryCode
        }

        binding.btnCallMe.setOnClickListener {
            if (binding.edPhoneNumber.text.toString().trim().length < 10) {
                GlobalUtils.shakeThisView(getmContext(), binding.editlayout)
            } else {
                GlobalUtils.showProgressDialog(getmContext())
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        viewModel.executeApi(
                            viewModel.getApiService()?.callRequest(
                                SharedPrefsHelper.getInstance(getmContext()).getUserData().accessToken,
                                prefix,
                                binding.edPhoneNumber.text.toString().trim(),
                                SharedPrefsHelper.getInstance(getmContext()).getUserData()._id
                            )
                        ).collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                    GlobalUtils.showProgressDialog(getmContext())
                                }
                                ApiResult.Status.ERROR -> {
                                    GlobalUtils.hideProgressDialog()
                                    GlobalUtils.showToastInCenter(getmContext(), it.message.toString())
                                }
                                ApiResult.Status.SUCCESS -> {
                                    GlobalUtils.hideProgressDialog()
                                    binding.ivBigDemoPic.visibility = View.VISIBLE
                                    binding.ivChecked.visibility = View.VISIBLE
                                    binding.tvSucceed.visibility = View.VISIBLE
                                    binding.ivSmallDemoPic.visibility = View.GONE
                                    binding.enterPhoneText.visibility = View.GONE
                                    binding.editlayout.visibility = View.GONE
                                    binding.btnCallMe.visibility = View.GONE
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalUtils.showToastInCenter(getmContext(), e.toString())
                    }
                }
            }
        }
    }
}