package com.transport.mall.ui.home.customers

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowCustomerListBinding
import com.transport.mall.databinding.RowNotificationsBinding
import com.transport.mall.databinding.RowSideMenuBinding
import com.transport.mall.model.CustomerModel
import com.transport.mall.model.NotificationModel
import com.transport.mall.model.SideMenu
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class CustomersAdapter(
    val context: Context,
    val dataList: List<CustomerModel>,
    val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowCustomerListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.data = dataList[position]
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            callBack.onResponse(position) }
        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_customer_list
    }
}