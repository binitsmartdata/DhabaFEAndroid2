package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAmenitiesBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.home.CommonActivity
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AmenitiesFragment :
    BaseFragment<FragmentAmenitiesBinding, AmenititesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_amenities
    override var viewModel: AmenititesVM
        get() = setUpVM(this, AmenititesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var userModel: UserModel? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity

        binding.isUpdate = mListener?.isUpdate()!!
        binding.viewOnly = mListener?.viewOnly()!!
        userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()
        binding.userModel = userModel

        fetchAmenitiesData()
    }

    private fun fetchAmenitiesData() {
        viewModel.getAllAmenities { amenitiesList ->
            amenitiesList.forEach {
                if (it.isFoodAmenities()) {
                    binding.foodAmenities = it
                }
                if (it.isParkingAmenities()) {
                    binding.parkingAmenities = it
                }
                if (it.isSleepingAmenities()) {
                    binding.sleepingAmenities = it
                }
                if (it.isWashroomAmenities()) {
                    binding.washroomAmenities = it
                }
                if (it.isSecurityAmenities()) {
                    binding.securityAmenities = it
                }
                if (it.isLightAmenities()) {
                    binding.lightAmenities = it
                }
                if (it.isOtherAmenities()) {
                    binding.otherAmenities = it
                }
            }
        }
    }

    override fun initListeners() {
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                binding.internalProgressBar.visibility = View.VISIBLE
            } else {
                binding.internalProgressBar.visibility = View.GONE
            }
        })
        progressObserver.observe(this, Observer {
            if (it) showProgressDialog() else hideProgressDialog()
        })

        binding.btnNext.setOnClickListener {
//            if (userModel?.isExecutive()!!) {
            if (isHavingPreviousData()) {
                mListener?.showNextScreen()
            }
            /*} else {
                if (isHavingPreviousData()) {
                    var ownerMissingParams = ""
                    var dhabaMissingParams = ""
                    mListener?.getDhabaModelMain()?.ownerModel?.let {
                        ownerMissingParams = it.getMissingParameters(getmContext()).trim()
                    }
                    mListener?.getDhabaModelMain()?.dhabaModel?.let {
                        dhabaMissingParams = it.getMissingParameters(getmContext()).trim()
                    }
                    if (ownerMissingParams.isNotEmpty() || dhabaMissingParams.isNotEmpty()) {
                        GlobalUtils.showInfoDialog(getmContext(), getString(R.string.details_missing_validation),
                            ownerMissingParams + "\n" + dhabaMissingParams,
                            GenericCallBack {
                                if (ownerMissingParams.isNotEmpty()) {
                                    mListener?.showOwnerScreen()
                                } else if (dhabaMissingParams.isNotEmpty()) {
                                    mListener?.showDhabaScreen()
                                } else {
                                    mListener?.showOwnerScreen()
                                }
                            })
                    } else {
                        updateDhabaStatus(false)
                    }
                }
            }*/
        }
        binding.btnSaveDraft.setOnClickListener {
            mListener?.getDhabaModelMain()?.dhabaModel?.let {
                // UPDATING DHABA STATUS TO ISDRAFT
                updateDhabaStatus(true)
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
        setRxBusListener()
    }

    private fun setRxBusListener() {
        //LISTENER TO LISTEN WHEN TO EXECUTE SAVE BUTTON
        RxBus.listen(DhabaModelMain.ActiveScreen::class.java).subscribe {
            if (it == DhabaModelMain.ActiveScreen.AmenitiesFragment) {
                try {
                    binding.btnNext.performClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateDhabaStatus(isDraft: Boolean) {
        viewModel.updateDhabaStatus(
            activity as Context,
            isDraft,
            mListener?.getDhabaModelMain()?.dhabaModel!!,
            if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS,
            progressObserver,
            GenericCallBack {
                if (it.data != null) {
                    mListener?.getDhabaModelMain()?.dhabaModel = it.data
                    if (isDraft) {
                        mListener?.getDhabaModelMain()?.draftedAtScreen =
                            DhabaModelMain.DraftScreen.AmenitiesFragment.toString()
                        mListener?.saveAsDraft()
                        activity?.finish()
                    } else {
                        showSuccessDialog(mListener?.getDhabaModelMain()?.dhabaModel?._id!!)
                    }
                } else {
                    showToastInCenter(it.message)
                }
            })
    }

    fun showSuccessDialog(dhabaId: String) {
        CommonActivity.showDhabaSuccessMessage(getmContext(), CommonActivity.TYPE_DHABA_SUCCESS, dhabaId)
        activity?.finish()
    }

    override fun onResume() {
        super.onResume()
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvFoodAmens, binding.icTickFood)
        }
        mListener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvParkingAmens, binding.icTickParking)
        }
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvSleepingAmen, binding.icTickSleeping)
        }
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvWashroomAmen, binding.icTickWashroom)
        }
        mListener?.getDhabaModelMain()?.securityAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvSecurityAmen, binding.icTickSecurity)
        }
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvLightAmen, binding.icTickLight)
        }
        mListener?.getDhabaModelMain()?.otherAmenitiesModel?.let {
            setSelectectedTextAndStyle(binding.tvOtherAmen, binding.icTickOther)
        }
    }

    private fun setSelectectedTextAndStyle(tv: TextView, ivTickView: AppCompatImageView) {
        tv.text = getString(R.string.amenities_selected)
        tv.setTextColor(ContextCompat.getColor(getmContext(), R.color.grey_amenities))
        ivTickView.visibility = View.VISIBLE
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