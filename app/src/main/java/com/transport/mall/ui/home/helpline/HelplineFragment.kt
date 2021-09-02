package com.transport.mall.ui.home.helpline

import com.transport.mall.R
import com.transport.mall.databinding.FragmentHelplineBinding
import com.transport.mall.model.DhabaDetailsModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.recyclerviewbase.RecyclerBindingList

/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class HelplineFragment : BaseFragment<FragmentHelplineBinding, BaseVM>(){
    override val layoutId: Int
        get() = R.layout.fragment_helpline
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentHelplineBinding
        get() = setUpBinding()
        set(value) {}

    private val bindList = RecyclerBindingList<DhabaDetailsModel>()

    override fun bindData() {

    }

    override fun initListeners() {

    }
}