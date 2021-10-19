package com.transport.mall.ui.home.offers

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.FragmentOffersBinding
import com.transport.mall.model.DiscountOfferModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class OffersFragment : BaseFragment<FragmentOffersBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_offers
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOffersBinding
        get() = setUpBinding()
        set(value) {}

    private lateinit var activeOffersAdapter: DiscountsOffersAdapter
    private lateinit var rejectedOffersAdapter: DiscountsOffersAdapter
    private lateinit var expiredOffersAdapter: DiscountsOffersAdapter

    private val activeOffers = ArrayList<DiscountOfferModel>()
    private val rejectedOffers = ArrayList<DiscountOfferModel>()
    private val expiredOffers = ArrayList<DiscountOfferModel>()

    override fun bindData() {
        setupActiveList()
        setupRejectedList()
        setupExpiredList()
        setHasOptionsMenu(false)
    }

    private fun setupActiveList() {
        activeOffers.clear()
        for (i in 0..2) {
            activeOffers.add(DiscountOfferModel("Test$i", "Expiring in 2 Days",
                true, R.drawable.ic_default_avatar))
        }
        activeOffersAdapter = DiscountsOffersAdapter(activity as Context, activeOffers,
            true, GenericCallBack {
            })

        binding.rvActive.layoutManager = LinearLayoutManager(getmContext())
        binding.rvActive.adapter = activeOffersAdapter

        refreshListAndNoDataViewActive()
    }

    private fun refreshListAndNoDataViewActive() {
        activeOffersAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.tvActive.visibility = View.VISIBLE
                binding.rvActive.visibility = View.VISIBLE
            } else {
                binding.tvActive.visibility = View.GONE
                binding.rvActive.visibility = View.GONE
            }
        }
    }

    private fun setupRejectedList() {
        rejectedOffers.clear()
        for (i in 0..2) {
            rejectedOffers.add(DiscountOfferModel("Test$i", "Expiring in 2 Days",
                true, R.drawable.ic_default_avatar))
        }
        rejectedOffersAdapter = DiscountsOffersAdapter(activity as Context, activeOffers,
            true, GenericCallBack {
            })

        binding.rvRejected.layoutManager = LinearLayoutManager(getmContext())
        binding.rvRejected.adapter = rejectedOffersAdapter

        refreshListAndNoDataViewRejected()
    }

    private fun refreshListAndNoDataViewRejected() {
        rejectedOffersAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.tvRejected.visibility = View.VISIBLE
                binding.rvRejected.visibility = View.VISIBLE
            } else {
                binding.tvRejected.visibility = View.GONE
                binding.rvRejected.visibility = View.GONE
            }
        }
    }

    private fun setupExpiredList() {
        expiredOffers.clear()
        for (i in 0..2) {
            expiredOffers.add(DiscountOfferModel("Test$i", "Expired on 8th Aug",
                false, R.drawable.ic_default_avatar))
        }
        expiredOffersAdapter = DiscountsOffersAdapter(activity as Context, expiredOffers,
            true, GenericCallBack {
            })

        binding.rvExpired.layoutManager = LinearLayoutManager(getmContext())
        binding.rvExpired.adapter = expiredOffersAdapter

        refreshListAndNoDataViewExpired()
    }

    private fun refreshListAndNoDataViewExpired() {
        expiredOffersAdapter.dataList.let {
            if (it.isNotEmpty()) {
                binding.tvExpired.visibility = View.VISIBLE
                binding.rvExpired.visibility = View.VISIBLE
            } else {
                binding.tvExpired.visibility = View.GONE
                binding.rvExpired.visibility = View.GONE
            }
        }

        if(activeOffers.size <= 0 && rejectedOffers.size <= 0 && expiredOffers.size <= 0){
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