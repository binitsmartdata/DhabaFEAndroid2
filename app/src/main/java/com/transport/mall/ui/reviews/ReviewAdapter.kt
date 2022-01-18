package com.transport.mall.ui.reviews

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.ReviewAdapterBinding
import com.transport.mall.model.ReviewModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class ReviewAdapter(
    val context: Context,
    val dataList: ArrayList<ReviewModel>,
    private val replyCallBack: GenericCallBack<Int>,
    val isPreviewMode: Boolean
) : InfiniteAdapter<ReviewAdapterBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.review = dataList.get(position)
        setButtonsClicks(myViewHolderG, position)
        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setButtonsClicks(myViewHolderG: MyViewHolderG?, position: Int) {
        myViewHolderG?.binding?.tvReply?.setOnClickListener {
            replyCallBack.onResponse(position)
        }
    }

    override fun getCount(): Int {
        if (isPreviewMode) {
            return if (dataList.size >= 2) 2 else dataList.size
        } else {
            return dataList.size
        }
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.review_adapter
    }
}