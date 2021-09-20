package com.transport.mall.ui.home.dhabalist

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowDhabaListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

class DhabaListAdapter(
    val context: Context, val dataList: List<DhabaModelMain>, val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowDhabaListBinding>() {

    init {
        setShouldLoadMore(true)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.model = dataList[position].dhabaModel
        myViewHolderG?.binding?.user = SharedPrefsHelper.getInstance(context).getUserData()
        myViewHolderG?.binding?.dhabaContainer?.setOnClickListener { callBack.onResponse(position) }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_dhaba_list
    }
}