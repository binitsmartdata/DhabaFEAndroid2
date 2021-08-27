package com.transport.mall.ui.home.helpline

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.FragmentHelplineBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.NotificationModel
import com.transport.mall.ui.home.notifications.NotificationsAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerBindingList
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

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

    private val bindList = RecyclerBindingList<DhabaModel>()

    override fun bindData() {

    }

    override fun initListeners() {

    }
}