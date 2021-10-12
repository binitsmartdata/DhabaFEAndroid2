package com.transport.mall.ui.customdialogs

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowUserListBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class UserListAdapter(val context: Context, val dataList: List<UserModel>, val callBack: GenericCallBack<UserModel>) : InfiniteAdapter<RowUserListBinding>() {

    init {
        setShouldLoadMore(true)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.model = dataList[position]
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            callBack.onResponse(dataList[position])
        }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_user_list
    }
}