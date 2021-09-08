package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAddDhabaStep3Binding
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.utils.base.BaseFragment

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
    }

    override fun initListeners() {
        binding.btnNext.setOnClickListener {
            mlistener?.showNextScreen()
        }

        binding.cardFood.setOnClickListener {
//            mlistener?.getDhabaModelMain()?.dhabaModel?.let {
            AmenitiesActivity.start(
                activity as Context,
                AmenitiesActivity.FOOD,
                mlistener?.getDhabaModelMain()!!
            )
//            }
        }
        binding.cardParking.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.PARKING,
                    it
                )
            }
        }
        binding.cardSleeping.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SLEEPING,
                    it
                )
            }
        }
        binding.cardWashroom.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.WASHROOM,
                    it
                )
            }
        }
        binding.cardSecurity.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SECURITY,
                    it
                )
            }
        }
        binding.cardOther.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.OTHER,
                    it
                )
            }
        }
        binding.cardLights.setOnClickListener {
            mlistener?.getDhabaModelMain()?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.LIGHT,
                    it
                )
            }
        }
    }
}