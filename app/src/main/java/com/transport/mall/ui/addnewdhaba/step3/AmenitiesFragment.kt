package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAmenitiesBinding
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AmenitiesFragment :
    BaseFragment<FragmentAmenitiesBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_amenities
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity

        binding.btnNext.isEnabled = !mListener?.isUpdate()!!
        binding.btnSaveDraft.isEnabled = !mListener?.isUpdate()!!
        binding.isUpdate = mListener?.isUpdate()!!
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
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardParking.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.PARKING,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardSleeping.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SLEEPING,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardWashroom.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.WASHROOM,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardSecurity.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.SECURITY,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardOther.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.OTHER,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
        binding.cardLights.setOnClickListener {
            AmenitiesActivity.start(
                activity as Context, AmenitiesActivity.LIGHT,
                mListener?.getDhabaModelMain()!!,
                mListener?.isUpdate()!!
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvFoodAmens)
        }
        mListener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvParkingAmens)
        }
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvSleepingAmen)
        }
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvWashroomAmen)
        }
        mListener?.getDhabaModelMain()?.securityAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvSecurityAmen)
        }
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvLightAmen)
        }
        mListener?.getDhabaModelMain()?.otherAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvOtherAmen)
        }
    }

    private fun setSelectectedTextAndStyle(tv: TextView) {
        tv.text = getString(R.string.amenities_selected)
        tv.setTextColor(ContextCompat.getColor(getmContext(), R.color.black))
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