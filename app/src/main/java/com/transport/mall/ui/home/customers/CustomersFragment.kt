package com.transport.mall.ui.home.customers

import android.app.Activity
import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentCustomersBinding
import com.transport.mall.model.CustomerModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class CustomersFragment : BaseFragment<FragmentCustomersBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_customers
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentCustomersBinding
        get() = setUpBinding()
        set(value) {}

    private lateinit var customersAdapter: CustomersAdapter
    var mCommonActivityListener: CommonActivityListener? = null

    override fun bindData() {
        setupCustomersList()
        setHasOptionsMenu(true)
    }

    private fun setupCustomersList() {
        val list = ArrayList<CustomerModel>()
//        for (i in 0..5) {
//            list.add(
//                CustomerModel(
//                    "Test$i",
//                    "12345678",
//                    R.drawable.ic_default_avatar
//                )
//            )
//        }
        customersAdapter = CustomersAdapter(activity as Context, list, GenericCallBack {
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(getmContext())
        binding.recyclerView.adapter = customersAdapter

        refreshListAndNoDataView()
    }

    private fun refreshListAndNoDataView() {
        customersAdapter.dataList.let {
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
        binding.bAddNew.setOnClickListener {
            DialogAddCustomer(activity as Context).show()
        }
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