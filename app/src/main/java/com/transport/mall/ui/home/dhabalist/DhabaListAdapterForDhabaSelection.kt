package com.transport.mall.ui.home.dhabalist

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowDhabaListForDhabaSelectonBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

class DhabaListAdapterForDhabaSelection(
    val context: Context,
    val dataList: List<DhabaModelMain>,
    val selectedTab: String?,
    private val deletionCallBack: GenericCallBack<DhabaModel>,
    private val editCallBack: GenericCallBack<Int>,
    private val viewCallBack: GenericCallBack<Int>,
    private val sendForApprovalCallBack: GenericCallBack<Int>
) : InfiniteAdapter<RowDhabaListForDhabaSelectonBinding>() {

    var userModel = UserModel()

    init {
        setShouldLoadMore(true)
        userModel = SharedPrefsHelper.getInstance(context).getUserData()
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.isInProgress = getDhabaModelAsPerTab(position)?.status.equals(DhabaModel.STATUS_INPROGRESS)
        myViewHolderG?.binding?.model = getDhabaModelAsPerTab(position)
        myViewHolderG?.binding?.owner = dataList[position].ownerModel
        myViewHolderG?.binding?.manager = dataList[position].manager
        myViewHolderG?.binding?.user = userModel

        setButtonsClicks(myViewHolderG, position)

        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setButtonsClicks(myViewHolderG: MyViewHolderG?, position: Int) {
        myViewHolderG?.binding?.root?.setOnClickListener {
            viewCallBack.onResponse(position)
        }
    }

    fun getDhabaModelAsPerTab(position: Int): DhabaModel? {
        if (selectedTab.equals(DhabaModel.STATUS_ACTIVE)
            && dataList[position].dhabaModel?.dhabaObj != null
            && GlobalUtils.getNonNullString(dataList[position].dhabaModel?.dhabaObj?.name, "").isNotEmpty()
        ) {
            return dataList[position].dhabaModel?.dhabaObj
        } else {
            return dataList[position].dhabaModel
        }
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_dhaba_list_for_dhaba_selecton
    }
}