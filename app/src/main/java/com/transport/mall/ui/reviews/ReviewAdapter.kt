package com.transport.mall.ui.reviews

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowDhabaListBinding
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class ReviewAdapter(val context: Context, private val natigateCallBack: GenericCallBack<Int>) :
    InfiniteAdapter<RowDhabaListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        /* myViewHolderG?.binding?.isInProgress = dataList[position].dhabaModel?.status.equals(
             DhabaModel.STATUS_INPROGRESS)
         myViewHolderG?.binding?.model = dataList[position].dhabaModel
         myViewHolderG?.binding?.owner = dataList[position].ownerModel
         myViewHolderG?.binding?.manager = dataList[position].manager*/



        setButtonsClicks(myViewHolderG, position)
        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setButtonsClicks(myViewHolderG: MyViewHolderG?, position: Int) {
        myViewHolderG?.binding?.parentItem?.setOnClickListener {
            natigateCallBack.onResponse(position)
        }
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.review_adapter
    }
}