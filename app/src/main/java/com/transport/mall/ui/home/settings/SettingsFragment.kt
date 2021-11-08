package com.transport.mall.ui.home.settings

import android.content.Context
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentSettingsBinding
import com.transport.mall.ui.customdialogs.DialogLanguageSelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentSettingsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: CommonActivityListener? = null

    override fun bindData() {
        mListener = activity as CommonActivityListener
        binding.profileLayout.setOnClickListener {
            mListener?.openProfileScreen()
        }
        binding.llLogout.setOnClickListener {
            SharedPrefsHelper.getInstance(activity as Context).clearData()
            mListener?.startOver()
        }
        binding.llLanguage.setOnClickListener {
            if (activity != null) {
                DialogLanguageSelection(getmContext(), this).show()
            }
        }

        val selectedLanguage = SharedPrefsHelper.getInstance(getmContext()).getSelectedLanguage()
        if (selectedLanguage.equals("hi"))
            binding.languageText.text = getString(R.string.hindi)
        else if (selectedLanguage.equals("pa"))
            binding.languageText.text = getString(R.string.punjabi)
        else
            binding.languageText.text = getString(R.string.english)
    }

    override fun initListeners() {

    }
}