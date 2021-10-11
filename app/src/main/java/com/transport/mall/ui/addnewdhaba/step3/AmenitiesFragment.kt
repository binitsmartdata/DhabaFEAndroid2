package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAmenitiesBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack

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
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity

        binding.btnNext.isEnabled = !mListener?.isUpdate()!!
        binding.btnSaveDraft.isEnabled = !mListener?.isUpdate()!!
        binding.isUpdate = mListener?.isUpdate()!!
    }

    override fun initListeners() {
        progressObserver.observe(this, Observer {
            if (it) showProgressDialog() else hideProgressDialog()
        })

        binding.btnNext.setOnClickListener {
            if (isHavingPreviousData()) {
                mListener?.showNextScreen()
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                // UPDATING DHABA STATUS TO ISDRAFT
                viewModel.updateDhabaStatus(true, it, null, progressObserver,
                    GenericCallBack {
                        if (it.data != null) {
                            mListener?.getDhabaModelMain()?.dhabaModel = it.data

                            mListener?.getDhabaModelMain()?.draftedAtScreen =
                                DhabaModelMain.DraftScreen.AmenitiesFragment.toString()
                            mListener?.saveAsDraft()
                            activity?.finish()
                        } else {
                            showToastInCenter(it.message)
                        }
                    })
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }

        binding.cardFood.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context,
                    AmenitiesActivity.FOOD,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardParking.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.PARKING,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardSleeping.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SLEEPING,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardWashroom.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.WASHROOM,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardSecurity.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.SECURITY,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardOther.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.OTHER,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
        }
        binding.cardLights.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                AmenitiesActivity.start(
                    activity as Context, AmenitiesActivity.LIGHT,
                    mListener?.getDhabaModelMain()!!,
                    mListener?.isUpdate()!!,
                    mListener?.viewOnly()!!
                )
            } ?: kotlin.run {
                showToastInCenter(getString(R.string.enter_dhaba_details_first))
            }
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