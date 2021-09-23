package com.transport.mall.ui.addnewdhaba.step3.amenities

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowPhotosBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class ImageGalleryAdapter(val context: Context, val dataList: ArrayList<PhotosModel>, val callBack: GenericCallBack<Int>) : InfiniteAdapter<RowPhotosBinding>() {

    var mDeletionListener: GenericCallBack<String>? = null

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.data = dataList.get(position)
        myViewHolderG?.binding?.ivCross?.setOnClickListener {
            if (dataList.get(position)._id.isNotEmpty()) {
                mDeletionListener?.let {
                    it.onResponse(dataList.get(position)._id)
                }
            }
            dataList.removeAt(position)
            callBack.onResponse(position)
            notifyDataSetChanged()
        }
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