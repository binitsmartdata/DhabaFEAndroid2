package com.transport.mall.ui.addnewdhaba.step1

import android.os.Handler
import com.transport.mall.R
import com.transport.mall.databinding.FragmentAddDhabaStep3Binding
import com.transport.mall.ui.authentication.login.LoginFragment
import com.transport.mall.utils.base.BaseFragment

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep3Fragment :
    BaseFragment<FragmentAddDhabaStep3Binding, AddDhabaStep2VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step3
    override var viewModel: AddDhabaStep2VM
        get() = setUpVM(this, AddDhabaStep2VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep3Binding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
//        binding.vm = viewModel
        binding.context = activity
    }

    override fun initListeners() {

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