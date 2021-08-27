package com.transport.mall.ui.home.notifications

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.FragmentNotificationsBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.NotificationModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerBindingList
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_notifications
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentNotificationsBinding
        get() = setUpBinding()
        set(value) {}

    private val bindList = RecyclerBindingList<DhabaModel>()

    override fun bindData() {
        setupNotificationList()
        setHasOptionsMenu(true)
    }

    override fun initListeners() {

    }

    private fun setupNotificationList() {
        val list = ArrayList<NotificationModel>()
        val menuArray = resources.getStringArray(R.array.notification_list_demo)
        for (i in menuArray.indices) {
            list.add(
                NotificationModel(
                    menuArray[i],
                    "Administrator",
                    "30 Minutes",
                    R.drawable.ic_message
                )
            )
        }
        binding.recyclerView.adapter =
            NotificationsAdapter(activity as Context, list, GenericCallBack {

            })
        binding.recyclerView.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
    }

    override fun onItemClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onChildItemClick(view: View?, parentIndex: Int, childIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun itemAction(type: String?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                showToastInCenter(getString(R.string.notifications))
                return true
            }
        }
        return false
    }
}