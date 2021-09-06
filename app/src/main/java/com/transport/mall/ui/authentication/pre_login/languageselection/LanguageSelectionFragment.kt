package com.transport.mall.ui.authentication.pre_login.languageselection

import android.os.Handler
import com.transport.mall.R
import com.transport.mall.databinding.FragmentLanguageSelectionBinding
import com.transport.mall.ui.authentication.login.LoginFragment
import com.transport.mall.utils.base.BaseFragment

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class LanguageSelectionFragment :
    BaseFragment<FragmentLanguageSelectionBinding, LanguageSelectionVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_language_selection
    override var viewModel: LanguageSelectionVM
        get() = setUpVM(this, LanguageSelectionVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentLanguageSelectionBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.vm = viewModel
        binding.context = activity
    }

    override fun initListeners() {
        binding.flEnglish.setOnClickListener {
            binding.english = true
            binding.hindi = false
            binding.punjabi = false
            setLanguageToEnglish()
            goToLogin()
        }
        binding.flHindi.setOnClickListener {
            binding.english = false
            binding.hindi = true
            binding.punjabi = false
            setLanguageToHindi()
            goToLogin()
        }
        binding.flPunjabi.setOnClickListener {
            binding.english = false
            binding.hindi = false
            binding.punjabi = true
            setLanguageToPunjabi()
            goToLogin()
        }
    }

    private fun goToLogin() {
        Handler().postDelayed(Runnable {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                LoginFragment(),
                "",
                true
            )
        }, 500)
    }
}