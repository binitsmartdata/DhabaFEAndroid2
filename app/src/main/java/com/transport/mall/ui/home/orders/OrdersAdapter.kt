package com.transport.mall.ui.home.orders

import android.content.Context
import androidx.core.content.ContextCompat
import com.transport.mall.R
import com.transport.mall.databinding.RowCustomerListBinding
import com.transport.mall.databinding.RowNotificationsBinding
import com.transport.mall.databinding.RowOrderListBinding
import com.transport.mall.databinding.RowSideMenuBinding
import com.transport.mall.model.CustomerModel
import com.transport.mall.model.NotificationModel
import com.transport.mall.model.OrderModel
import com.transport.mall.model.SideMenu
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class OrdersAdapter(
    val context: Context,
    val dataList: List<OrderModel>,
    val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowOrderListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.data = dataList[position]
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            callBack.onResponse(position) }
        myViewHolderG?.binding?.executePendingBindings()

        myViewHolderG?.binding?.tvStatus?.setTextColor(when (dataList[position].status) {
            "Shipped" -> {
                ContextCompat.getColor(context, R.color.colorAccent)
            }
            "Delivered" -> {
                ContextCompat.getColor(context, R.color.greenText)
            }
            "Canceled" -> {
                ContextCompat.getColor(context, R.color.black)
            }
            else -> {
                ContextCompat.getColor(context, R.color.colorAccent)
            }
        })
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_order_list
    }
}