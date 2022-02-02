package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.content.Intent
import android.net.Uri
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
    val selectedTab: String?,
    private val deletionCallBack: GenericCallBack<DhabaModel>,
    private val editCallBack: GenericCallBack<Int>,
    private val viewCallBack: GenericCallBack<Int>,
    private val sendForApprovalCallBack: GenericCallBack<Int>
) : InfiniteAdapter<RowDhabaListBinding>() {

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

        manageIconsVisibility(myViewHolderG, position)

        if (getDhabaModelAsPerTab(position)?.dhabaCategory.equals(getDhabaModelAsPerTab(position)?.CATEGORY_GOLD, true)) {
            myViewHolderG?.binding?.tvCategory?.setBackgroundResource(R.drawable.ic_gold_hotel_type)
        } else if (getDhabaModelAsPerTab(position)?.dhabaCategory.equals(getDhabaModelAsPerTab(position)?.CATEGORY_BRONZE, true)) {
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
                    deletionCallBack.onResponse(getDhabaModelAsPerTab(position))
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
//            val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", getDhabaModelAsPerTab(position)!!.latitude, getDhabaModelAsPerTab(position)!!.longitude)
            val geoUri =
                "http://maps.google.com/maps?q=loc:" + getDhabaModelAsPerTab(position)!!.latitude.toString() + "," + getDhabaModelAsPerTab(position)!!.longitude.toString() + " (" + getDhabaModelAsPerTab(
                    position
                )!!.name + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            context.startActivity(intent)
        }
        myViewHolderG?.binding?.llAssignMgr?.setOnClickListener {
            if (dataList[position].manager == null) {
                DailogAddManager(context, dataList[position].ownerModel!!, getDhabaModelAsPerTab(position)!!, GenericCallBack {
                    if (it != null) {
                        dataList[position].manager = it
                        notifyDataSetChanged()
                        RxBus.publish(getDhabaModelAsPerTab(position)!!)
                    }
                }).show()
            }
        }
        myViewHolderG?.binding?.tvSendForApproval?.setOnClickListener {
            sendForApprovalCallBack.onResponse(position)
        }
    }

    private fun manageIconsVisibility(myViewHolderG: MyViewHolderG?, position: Int) {
        val latitude = GlobalUtils.getNonNullString(getDhabaModelAsPerTab(position)?.latitude, "0")
        val longitude = GlobalUtils.getNonNullString(getDhabaModelAsPerTab(position)?.longitude, "0")

        var dhabaStatus = ""
        //  ANALYSING THE CORRECT STATUS OF DHABA
        if (selectedTab != null) {
            dhabaStatus = selectedTab
        } else {
            if (getDhabaModelAsPerTab(position)?.status.equals(DhabaModel.STATUS_PENDING)) {
                if (getDhabaModelAsPerTab(position)?.draft_by.equals(SharedPrefsHelper.getInstance(context).getUserData()._id)) {
                    dhabaStatus = DhabaModel.STATUS_PENDING
                } else {
                    dhabaStatus = DhabaModel.STATUS_INPROGRESS
                }
            } else {
                dhabaStatus = getDhabaModelAsPerTab(position)?.status.toString()
            }
        }
        myViewHolderG?.binding?.dhabaStatus = dhabaStatus
        //-----------------

        when (dhabaStatus) {
            DhabaModel.STATUS_PENDING -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                myViewHolderG?.binding?.ivEdit?.visibility = View.VISIBLE
                myViewHolderG?.binding?.ivLocation?.visibility = if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) View.VISIBLE else View.GONE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
            DhabaModel.STATUS_INPROGRESS -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                myViewHolderG?.binding?.ivEdit?.visibility =
                    if (SharedPrefsHelper.getInstance(context).getUserData().isExecutive()
                        && getDhabaModelAsPerTab(position)?.approval_for.equals(UserModel.ROLE_EXECUTIVE)
                    ) View.VISIBLE else View.GONE
                myViewHolderG?.binding?.ivLocation?.visibility = if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) View.VISIBLE else View.GONE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
            DhabaModel.STATUS_ACTIVE -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                // IF THE DHABA WAS ACTIVE BUT ITS DRAFTED BY ANOTHER PERSON. HIDE EDIT ICON
                if (selectedTab.equals(DhabaModel.STATUS_ACTIVE)) {
                    myViewHolderG?.binding?.ivEdit?.visibility =
                        if (dataList[position].dhabaModel?.status.equals(DhabaModel.STATUS_PENDING) && !dataList[position].dhabaModel?.draft_by.equals(userModel._id)) View.GONE
                        else View.VISIBLE
                } else {
                    myViewHolderG?.binding?.ivEdit?.visibility = View.VISIBLE
                }
                //------------------------
                myViewHolderG?.binding?.ivLocation?.visibility = if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) View.VISIBLE else View.GONE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
            DhabaModel.STATUS_INACTIVE, "" -> {
                myViewHolderG?.binding?.ivDelete?.visibility = View.GONE
                myViewHolderG?.binding?.ivEdit?.visibility = View.GONE
                myViewHolderG?.binding?.ivLocation?.visibility = if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) View.VISIBLE else View.GONE
                myViewHolderG?.binding?.ivView?.visibility = View.VISIBLE
            }
        }

        // hide edit icon if dhaba is not active
        if (getDhabaModelAsPerTab(position)?.isActiveDhaba()!!) {
            myViewHolderG?.binding?.ivEdit?.visibility = myViewHolderG?.binding?.ivEdit?.visibility!!
        } else {
            myViewHolderG?.binding?.ivEdit?.visibility = View.GONE
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
        return R.layout.row_dhaba_list
    }
}