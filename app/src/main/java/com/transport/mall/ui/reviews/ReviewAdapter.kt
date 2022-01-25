package com.transport.mall.ui.reviews

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.ReviewAdapterBinding
import com.transport.mall.model.ReportReasonModel
import com.transport.mall.model.ReviewModel
import com.transport.mall.ui.customdialogs.DailogReportReview
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

class ReviewAdapter(
    val context: Context,
    val dataList: ArrayList<ReviewModel>,
    private val replyCallBack: GenericCallBack<Int>,
    val reportReasons: ArrayList<ReportReasonModel>,
    val isPreviewMode: Boolean
) : InfiniteAdapter<ReviewAdapterBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.review = dataList.get(position)
        myViewHolderG?.binding?.loggedInUser = SharedPrefsHelper.getInstance(context).getUserData()
        setButtonsClicks(myViewHolderG, position)
        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setButtonsClicks(myViewHolderG: MyViewHolderG?, position: Int) {
        myViewHolderG?.binding?.tvReply?.setOnClickListener {
            replyCallBack.onResponse(position)
        }
        myViewHolderG?.binding?.tvReport?.setOnClickListener {
            DailogReportReview(
                context,
                SharedPrefsHelper.getInstance(context).getUserData(),
                dataList.get(position), reportReasons,{

                }).show()
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