package com.transport.mall.ui.home.settings

import com.transport.mall.R
import com.transport.mall.databinding.FragmentNotificationsBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.NotificationModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.recyclerviewbase.RecyclerBindingList

/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class SettingsFragment : BaseFragment<FragmentNotificationsBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentNotificationsBinding
        get() = setUpBinding()
        set(value) {}

    private val bindList = RecyclerBindingList<DhabaModel>()

    override fun bindData() {

    }

    override fun initListeners() {

    }
}