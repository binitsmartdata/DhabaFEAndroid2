package com.transport.mall.ui.home.helpline

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.FragmentEditProfileBinding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_edit_profile
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentEditProfileBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        setUserData()
    }

    private fun setUserData() {
        binding.userData = SharedPrefsHelper.getInstance(activity as Context).getUserData()
    }

    override fun initListeners() {

    }
}