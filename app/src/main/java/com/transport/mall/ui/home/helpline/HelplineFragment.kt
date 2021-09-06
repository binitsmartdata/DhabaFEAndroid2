package com.transport.mall.ui.home.helpline

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.FragmentHelplineBinding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class HelplineFragment : BaseFragment<FragmentHelplineBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_helpline
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentHelplineBinding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.callCardView.setOnClickListener {
            DialogRequestCall(activity as Context).show()
        }
    }

    override fun initListeners() {

    }
}