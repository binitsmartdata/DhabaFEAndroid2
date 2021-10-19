package com.transport.mall.ui.home.offers

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.FragmentDiscountsBinding
import com.transport.mall.model.DiscountOfferModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class DiscountsFragment : BaseFragment<FragmentDiscountsBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_discounts
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDiscountsBinding
        get() = setUpBinding()
        set(value) {}

    private lateinit var activeDiscountsAdapter: DiscountsOffersAdapter
    private lateinit var expiredDiscountsAdapter: DiscountsOffersAdapter
    private val activeDiscounts = ArrayList<DiscountOfferModel>()
    private val expiredDiscounts = ArrayList<DiscountOfferModel>()

    override fun bindData() {
        setupActiveList()
        setupExpiredList()
        setHasOptionsMenu(false)
    }

    private fun setupActiveList() {
        activeDiscounts.clear()
//        for (i in 0..2) {
//            activeDiscounts.add(DiscountModel("Test$i", "Expiring in 2 Days",
//                true, R.drawable.ic_default_avatar))
//        }
        activeDiscountsAdapter = DiscountsOffersAdapter(activity as Context, activeDiscounts,
            false, GenericCallBack {
        })

        binding.rvActive.layoutManager = LinearLayoutManager(getmContext())
        binding.rvActive.adapter = activeDiscountsAdapter

        refreshListAndNoDataViewActive()
    }

    private fun refreshListAndNoDataViewActive() {
        activeDiscountsAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.tvActive.visibility = View.VISIBLE
                binding.rvActive.visibility = View.VISIBLE
            } else {
                binding.tvActive.visibility = View.GONE
                binding.rvActive.visibility = View.GONE
            }
        }
    }

    private fun setupExpiredList() {
        expiredDiscounts.clear()
//        for (i in 0..2) {
//            expiredDiscounts.add(DiscountModel("Test$i", "Expired on 8th Aug",
//                false, R.drawable.ic_default_avatar))
//        }
        expiredDiscountsAdapter = DiscountsOffersAdapter(activity as Context, expiredDiscounts,
            true, GenericCallBack {
        })

        binding.rvExpired.layoutManager = LinearLayoutManager(getmContext())
        binding.rvExpired.adapter = expiredDiscountsAdapter

        refreshListAndNoDataViewExpired()
    }

    private fun refreshListAndNoDataViewExpired() {
        expiredDiscountsAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.tvExpired.visibility = View.VISIBLE
                binding.rvExpired.visibility = View.VISIBLE
            } else {
                binding.tvExpired.visibility = View.GONE
                binding.rvExpired.visibility = View.GONE
            }
        }

        if(activeDiscounts.size <= 0 && expiredDiscounts.size <= 0){
            binding.llNoData.visibility = View.VISIBLE
        }else{
            binding.llNoData.visibility = View.GONE
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