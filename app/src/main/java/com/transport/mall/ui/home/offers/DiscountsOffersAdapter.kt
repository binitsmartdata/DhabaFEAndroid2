package com.transport.mall.ui.home.offers

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.*
import com.transport.mall.model.*
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class DiscountsOffersAdapter(
    val context: Context,
    val dataList: List<DiscountOfferModel>,
    private val switchVisibility: Boolean,
    val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowDiscountOffersListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.isVisible = switchVisibility
        myViewHolderG?.binding?.data = dataList[position]
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            callBack.onResponse(position)
        }
        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_discount_offers_list
    }
}