package com.transport.mall.ui.home.shops

import android.app.Activity
import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentShopsBinding
import com.transport.mall.model.CustomerModel
import com.transport.mall.model.ShopModel
import com.transport.mall.ui.home.customers.CustomersAdapter
import com.transport.mall.ui.home.customers.DialogAddCustomer
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class ShopsFragment : BaseFragment<FragmentShopsBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_shops
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentShopsBinding
        get() = setUpBinding()
        set(value) {}

    private lateinit var shopAdapter: ShopsAdapter
    var mCommonActivityListener: CommonActivityListener? = null

    override fun bindData() {
        setupCustomersList()
        setHasOptionsMenu(true)
    }

    private fun setupCustomersList() {
        val list = ArrayList<ShopModel>()
        list.add(
            ShopModel("Test", 120, 0,
                R.drawable.ic_default_avatar))
        for (i in 0..2) {
            list.add(
                ShopModel("Test$i", 120, 1100,
                    R.drawable.ic_default_avatar))
        }
        shopAdapter = ShopsAdapter(activity as Context, list, GenericCallBack {
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(getmContext())
        binding.recyclerView.adapter = shopAdapter

        refreshListAndNoDataView()
    }

    private fun refreshListAndNoDataView() {
        shopAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.llNoData.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.edSearch.visibility = View.VISIBLE
            } else {
                binding.llNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.edSearch.visibility = View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                mCommonActivityListener?.openNotificationFragment()
                return true
            }
        }
        return false
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mCommonActivityListener = activity as CommonActivityListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement TransactionDataListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCommonActivityListener = null
    }

}