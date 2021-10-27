package com.transport.mall.ui.viewdhaba

import android.content.Context
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.RowPhotosBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class ImageGalleryAdapter(val context: Context, val dataList: ArrayList<PhotosModel>) : InfiniteAdapter<RowPhotosBinding>() {

    var mDeletionListener: GenericCallBack<String>? = null

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.data = dataList.get(position)
        myViewHolderG?.binding?.ivCross?.visibility = View.GONE
        myViewHolderG?.binding?.containerLayout?.setOnClickListener {
            ImagePagerActivity.start(context, dataList, position)
        }

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_photos
    }

    public fun setDeletionListener(listener: GenericCallBack<String>) {
        mDeletionListener = listener
    }
}