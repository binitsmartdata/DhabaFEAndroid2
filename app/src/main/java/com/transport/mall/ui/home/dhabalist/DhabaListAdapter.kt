package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.RowDhabaListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

class DhabaListAdapter(
    val context: Context,
    val dataList: List<DhabaModelMain>,
    val status: String,
    val callBack: GenericCallBack<Int>,
    val deletionCallBack: GenericCallBack<DhabaModel>
) : InfiniteAdapter<RowDhabaListBinding>() {

    init {
        setShouldLoadMore(true)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.isInProgress = status.equals(DhabaModel.STATUS_INPROGRESS)
        myViewHolderG?.binding?.model = dataList[position].dhabaModel
        myViewHolderG?.binding?.owner = dataList[position].ownerModel
        myViewHolderG?.binding?.user = SharedPrefsHelper.getInstance(context).getUserData()
        myViewHolderG?.binding?.dhabaContainer?.setOnClickListener { callBack.onResponse(position) }

        myViewHolderG?.binding?.ivDelete?.visibility = if (status.equals(DhabaModel.STATUS_PENDING)) View.VISIBLE else View.GONE
        myViewHolderG?.binding?.ivEdit?.visibility = if (status.equals(DhabaModel.STATUS_PENDING) || status.equals(DhabaModel.STATUS_INPROGRESS)) View.VISIBLE else View.GONE

        if (dataList[position].dhabaModel?.dhabaCategory.equals(dataList[position].dhabaModel?.CATEGORY_GOLD, true)) {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_gold_hotel_type)
        } else if (dataList[position].dhabaModel?.dhabaCategory.equals(dataList[position].dhabaModel?.CATEGORY_BRONZE, true)) {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_bronze_hotel_type)
        } else {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_silver_hotel_type)
        }

        myViewHolderG?.binding?.ivDelete?.setOnClickListener {
            GlobalUtils.showConfirmationDialogYesNo(context, context.getString(R.string.deletion_confirmation), GenericCallBack {
                if (it) {
                    deletionCallBack.onResponse(dataList[position].dhabaModel)
                }
            })
        }
        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_dhaba_list
    }
}