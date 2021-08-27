package com.transport.mall.ui.addnewdhaba.step3.amenities

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowPhotosBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter

class ImageGalleryAdapter(
    val context: Context, val dataList: List<PhotosModel>, val callBack: GenericCallBack<Int>
) : InfiniteAdapter<RowPhotosBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.position = position
        myViewHolderG?.binding?.containerLayout?.setOnClickListener { callBack.onResponse(position) }
        myViewHolderG?.binding?.data = dataList.get(position)

        myViewHolderG?.binding?.executePendingBindings()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_photos
    }
}