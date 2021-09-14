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
class AmenitiesFragment :
    BaseFragment<FragmentAddDhabaStep3Binding, OwnerDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step3
    override var viewModel: OwnerDetailsVM
        get() = setUpVM(this, OwnerDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep3Binding
        get() = setUpBinding()
        set(value) {}

    var mlistener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mlistener = activity as AddDhabaListener
        binding.context = activity
    }

    override fun initListeners() {
        binding.btnNext.setOnClickListener {
            mlistener?.showNextScreen()
        }

        binding.cardFood.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context,
                AmenitiesActivity.FOOD,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardParking.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.PARKING,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardSleeping.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SLEEPING,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardWashroom.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.WASHROOM,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardSecurity.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SECURITY,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardOther.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.OTHER,
                mlistener?.getDhabaModelMain()!!
            )
        }
        binding.cardLights.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.LIGHT,
                mlistener?.getDhabaModelMain()!!
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mlistener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            binding.tvFoodAmens.text = getString(R.string.amenities_selected)
        }
        mlistener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            binding.tvParkingAmens.text = getString(R.string.amenities_selected)
        }
        mlistener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            binding.tvSleepingAmen.text = getString(R.string.amenities_selected)
        }
        mlistener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            binding.tvWashroomAmen.text = getString(R.string.amenities_selected)
        }
    }

    fun youAreInFocus() {

    }
}