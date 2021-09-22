package com.transport.mall.ui.customdialogs

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowHighwayListBinding
import com.transport.mall.model.HighwayModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class HighwayListAdapter(
    val context: Context, val dataList: List<HighwayModel>, val listener: GenericCallBack<HighwayModel>
) : InfiniteAdapter<RowHighwayListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.highway = dataList[position]
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            listener.onResponse(dataList[position])
        }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_highway_list
    }
}