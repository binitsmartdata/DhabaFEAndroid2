package com.transport.mall.ui.home.sidemenu

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowSideMenuBinding
import com.transport.mall.model.SideMenu
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class SideMenuAdapter(
    val context: Context, val dataList: List<SideMenu>, val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowSideMenuBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.data = dataList[position]
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.containerLayout?.setOnClickListener { callBack.onResponse(position) }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_side_menu
    }
}