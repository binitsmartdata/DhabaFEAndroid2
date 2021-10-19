package com.transport.mall.ui.home.orders

import android.app.Activity
import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentPastOrdersBinding
import com.transport.mall.model.OrderModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class PastOrdersFragment : BaseFragment<FragmentPastOrdersBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_past_orders
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentPastOrdersBinding
        get() = setUpBinding()
        set(value) {}

    private lateinit var ordersAdapter: OrdersAdapter

    override fun bindData() {
        setupOrdersList()
        setHasOptionsMenu(false)
    }

    private fun setupOrdersList() {
        val list = ArrayList<OrderModel>()
        for (i in 0..5) {
            list.add(
                OrderModel(
                    "Test$i",
                    "12 Sep 2021",
                        "Delivered",
                    R.drawable.ic_default_avatar
                )
            )
        }
        ordersAdapter = OrdersAdapter(activity as Context, list, GenericCallBack {
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(getmContext())
        binding.recyclerView.adapter = ordersAdapter

        refreshListAndNoDataView()
    }

    private fun refreshListAndNoDataView() {
        ordersAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.llNoData.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.llNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    override fun initListeners() {

    }

    override fun onItemClick(view: View?, position: Int) {
    }

    override fun onChildItemClick(view: View?, parentIndex: Int, childIndex: Int) {
    }

    override fun itemAction(type: String?, position: Int) {
    }
}