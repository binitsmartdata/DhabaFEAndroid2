package com.transport.mall.ui.home.helpline

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentHelplineBinding
import com.transport.mall.ui.home.CommonActivity
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

    var mListener: CommonActivityListener? = null

    override fun bindData() {
        setHasOptionsMenu(true)
        mListener = activity as CommonActivityListener
        binding.callCardView.setOnClickListener {
            CommonActivity.start(getmContext(), CommonActivity.TYPE_REQUEST_FOR_CALL, "")
        }
    }

    override fun initListeners() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                mListener?.openNotificationFragment()
                return true
            }
        }
        return false
    }
}