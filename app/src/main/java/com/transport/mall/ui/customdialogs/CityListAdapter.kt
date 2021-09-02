package com.transport.mall.ui.customdialogs

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowCityListBinding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class CityListAdapter(
    val context: Context, val dataList: List<CityAndStateModel>
) : InfiniteAdapter<RowCityListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.data = dataList[position]
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            dataList.get(position).isChecked = !dataList.get(position).isChecked
            notifyDataSetChanged()
        }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_city_list
    }
}