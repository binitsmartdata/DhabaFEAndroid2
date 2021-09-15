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

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity
    }

    override fun initListeners() {
        binding.btnNext.setOnClickListener {
            if (isHavingPreviousData()) {
                mListener?.showNextScreen()
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            mListener?.saveAsDraft()
            activity?.finish()
        }

        binding.cardFood.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context,
                AmenitiesActivity.FOOD,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardParking.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.PARKING,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardSleeping.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SLEEPING,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardWashroom.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.WASHROOM,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardSecurity.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SECURITY,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardOther.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.OTHER,
                mListener?.getDhabaModelMain()!!
            )
        }
        binding.cardLights.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.LIGHT,
                mListener?.getDhabaModelMain()!!
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            binding.tvFoodAmens.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            binding.tvParkingAmens.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            binding.tvSleepingAmen.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            binding.tvWashroomAmen.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.securityAmenitiesModel?.let {
            binding.tvSecurityAmen.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            binding.tvLightAmen.text = getString(R.string.amenities_selected)
        }
        mListener?.getDhabaModelMain()?.otherAmenitiesModel?.let {
            binding.tvOtherAmen.text = getString(R.string.amenities_selected)
        }
    }

    private fun isHavingPreviousData(): Boolean {
        if (mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
            return false
        } else if (mListener?.getDhabaModelMain()?.dhabaModel == null) {
            showToastInCenter(getString(R.string.enter_dhaba_details))
            return false
        }
        return true
    }

    fun youAreInFocus() {

    }
}