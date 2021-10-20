package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.RowDhabaListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel
import com.transport.mall.ui.customdialogs.DailogAddManager
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

class DhabaListAdapter(
    val context: Context,
    val dataList: List<DhabaModelMain>,
    private val deletionCallBack: GenericCallBack<DhabaModel>,
    private val editCallBack: GenericCallBack<Int>,
    private val viewCallBack: GenericCallBack<Int>,
    private val locateCallBack: GenericCallBack<Int>
) : InfiniteAdapter<RowDhabaListBinding>() {

    var userModel = UserModel()

    init {
        setShouldLoadMore(true)
        userModel = SharedPrefsHelper.getInstance(context).getUserData()
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.isInProgress = dataList[position].dhabaModel?.status.equals(DhabaModel.STATUS_INPROGRESS)
        myViewHolderG?.binding?.model = dataList[position].dhabaModel
        myViewHolderG?.binding?.owner = dataList[position].ownerModel
        myViewHolderG?.binding?.manager = dataList[position].manager
        myViewHolderG?.binding?.user = userModel

        manageIconsVisibility(myViewHolderG, position)

        if (dataList[position].dhabaModel?.dhabaCategory.equals(dataList[position].dhabaModel?.CATEGORY_GOLD, true)) {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_gold_hotel_type)
        } else if (dataList[position].dhabaModel?.dhabaCategory.equals(dataList[position].dhabaModel?.CATEGORY_BRONZE, true)) {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_bronze_hotel_type)
        } else {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_silver_hotel_type)
        }

        setButtonsClicks(myViewHolderG, position)
        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setButtonsClicks(myViewHolderG: MyViewHolderG?, position: Int) {
        myViewHolderG?.binding?.ivDelete?.setOnClickListener {
            GlobalUtils.showConfirmationDialogYesNo(context, context.getString(R.string.deletion_confirmation), GenericCallBack {
                if (it) {
                    deletionCallBack.onResponse(dataList[position].dhabaModel)
                }
            })
        }

        myViewHolderG?.binding?.ivEdit?.setOnClickListener {
            editCallBack.onResponse(position)
        }

        myViewHolderG?.binding?.ivView?.setOnClickListener {
            viewCallBack.onResponse(position)
        }

        myViewHolderG?.binding?.ivLocation?.setOnClickListener {
            locateCallBack.onResponse(position)
        }
        myViewHolderG?.binding?.llAssignMgr?.setOnClickListener {
            if (dataList[position].manager == null) {
                DailogAddManager(context, dataList[position].dhabaModel!!, GenericCallBack {
                    if (it != null) {
                        dataList[position].manager = it
                        notifyDataSetChanged()
                        RxBus.publish(dataList[position].dhabaModel!!)
                    }
                }).show()
            }
        }
    }

    private fun manageIconsVisibility(myViewHolderG: MyViewHolderG?, position: Int) {
        when (dataList[position].dhabaModel?.status) {
            DhabaModel.STATUS_PENDING -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivEdit?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivLocation?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
            DhabaModel.STATUS_INPROGRESS -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                myViewHolderG?.binding?.ivEdit?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivLocation?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
            DhabaModel.STATUS_ACTIVE, DhabaModel.STATUS_INACTIVE, "" -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                myViewHolderG?.binding?.ivEdit?.visibility = View.GONE
                myViewHolderG?.binding?.ivLocation?.visibility = View.GONE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
        }
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_dhaba_list
    }
}