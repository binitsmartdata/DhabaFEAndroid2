package com.transport.mall.ui.home.settings

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentNotificationSettingsBinding
import com.transport.mall.databinding.FragmentSettingsBinding
import com.transport.mall.ui.customdialogs.DialogLanguageSelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class NotificationSettings : BaseFragment<FragmentNotificationSettingsBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_notification_settings
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentNotificationSettingsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: CommonActivityListener? = null

    companion object {
        val TAG = "NotificationSettings"
        var fragmentObj: NotificationSettings? = null

        fun getInstance(): NotificationSettings {
            fragmentObj?.let {
                return it
            } ?: kotlin.run {
                fragmentObj = NotificationSettings()
                return fragmentObj!!
            }
        }
    }

    override fun bindData() {
        mListener = activity as CommonActivityListener
        setHasOptionsMenu(true)

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