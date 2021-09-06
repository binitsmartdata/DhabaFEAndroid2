package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAddDhabaStep3Binding
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AddDhabaStep3Fragment :
    BaseFragment<FragmentAddDhabaStep3Binding, AddDhabaStep2VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step3
    override var viewModel: AddDhabaStep2VM
        get() = setUpVM(this, AddDhabaStep2VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep3Binding
        get() = setUpBinding()
        set(value) {}

    var mlistener: AddDhabaListener? = null

    override fun bindData() {
        mlistener = activity as AddDhabaListener
        binding.context = activity
        binding.cardFood.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context,
                    AmenitiesActivity.FOOD,
                    it._id
                )
            }
        }
        binding.cardParking.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.PARKING,
                    it._id
                )
            }
        }
        binding.cardSleeping.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SLEEPING,
                    it._id
                )
            }
        }
        binding.cardWashroom.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.WASHROOM,
                    it._id
                )
            }
        }
        binding.cardSecurity.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SECURITY,
                    it._id
                )
            }
        }
        binding.cardOther.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.OTHER,
                    it._id
                )
            }
        }
        binding.cardLights.setOnClickListener {
            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.LIGHT,
                    it._id
                )
            }
        }
    }

    override fun initListeners() {
        binding.btnSaveDhaba.setOnClickListener {
            DialogAddDhabaSuccess(
                activity as Context,
                "#LKJ534LK5J3L54J3L45",
                GenericCallBack {
                    when (it) {
                        DialogAddDhabaSuccess.SELECTED_ACTION.GO_HOME -> {
                            goToHomeScreen()
                        }
                        DialogAddDhabaSuccess.SELECTED_ACTION.VIEW_DHABA -> {

                        }
                    }
                }).show()
        }
    }
}