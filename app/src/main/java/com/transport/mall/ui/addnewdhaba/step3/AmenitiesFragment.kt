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
import com.transport.mall.database.DhabaTimingModelParent
import com.transport.mall.databinding.FragmentAmenitiesBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.addnewdhaba.step4.*
import com.transport.mall.ui.customdialogs.ConfirmationDialog
import com.transport.mall.ui.customdialogs.DialogSubmitforApproval
import com.transport.mall.ui.home.CommonActivity
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
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

        /*if (isExecutiveReviewingOwnerDhaba()) {
            binding.btnSaveDraft.visibility = View.INVISIBLE
        }*/
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
            if (isHavingPreviousData()) {
                mListener?.showNextScreen()
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            if (mListener?.getDhabaModelMain()?.ownerModel == null || GlobalUtils.getNonNullString(mListener?.getDhabaModelMain()?.ownerModel?.ownerName, "").isEmpty()) {
                showToastInCenter(getString(R.string.enter_owner_name))
                mListener?.showOwnerScreen()
            } else if (mListener?.getDhabaModelMain()?.dhabaModel == null || GlobalUtils.getNonNullString(mListener?.getDhabaModelMain()?.dhabaModel?.name, "").isEmpty()) {
                showToastInCenter(getString(R.string.enter_dhaba_name))
                mListener?.showDhabaScreen()
            } else {
                ConfirmationDialog(getmContext(), getString(R.string.are_you_sure_you_want_to_save_as_draft), GenericCallBack {
                    if (it) {
                        proceed(true)
                    }
                }).show()
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
                mListener?.showDhabaScreen()
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
                mListener?.showDhabaScreen()
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
                mListener?.showDhabaScreen()
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
                mListener?.showDhabaScreen()
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
                mListener?.showDhabaScreen()
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
                mListener?.showDhabaScreen()
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
            false,
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
            mListener?.showOwnerScreen()
            return false
        } else if (mListener?.getDhabaModelMain()?.dhabaModel == null) {
            showToastInCenter(getString(R.string.enter_dhaba_details))
            mListener?.showDhabaScreen()
            return false
        }
        return true
    }

    fun youAreInFocus() {

    }

    private fun proceed(isDraft: Boolean) {
        DialogSubmitforApproval(
            getmContext(),
            mListener?.getDhabaModelMain()!!,
            viewModel.submitForApprovalObservers
        ).show()

        viewModel.saveOwnerDetails(mListener?.getDhabaModelMain()?.ownerModel!!, GenericCallBack {
            if (it != null) {
                // Assigning Ids to respective models
                mListener?.getDhabaModelMain()?.dhabaModel?.owner_id = it._id
                mListener?.getDhabaModelMain()?.bankDetailsModel?.user_id = it._id

                //UPDATE OWNER'S DETAILS IN USER'S DATA BECAUSE OWNER IS THE SAME USER WHO HAS LOGGED IN
                if (binding.userModel!!.isOwner() && binding.userModel!!._id.equals(mListener?.getDhabaModelMain()?.ownerModel?._id)) {
                    binding.userModel!!.populateData(it)
                    SharedPrefsHelper.getInstance(getmContext()).setUserData(binding.userModel!!)
                    //NOTIFY THAT USER MODEL IS UPDATED
                    RxBus.publish(it)
                }
                //---------

                viewModel.saveDhabaDetails(mListener?.getDhabaModelMain()?.dhabaModel, GenericCallBack {
                    if (it != null) {
                        mListener?.getDhabaModelMain()?.dhabaModel?._id = it._id
                        val timingParentModel = DhabaTimingModelParent(it._id, mListener?.getDhabaModelMain()?.dhabaTiming)
                        viewModel.addDhabaTimeing(timingParentModel, GenericCallBack {
                            if (it) {
                                viewModel.saveFoodAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                    if (it) {
                                        viewModel.saveParkingAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                            if (it) {
                                                viewModel.saveSleepingAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                                    if (it) {
                                                        viewModel.saveWashroomAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                                            if (it) {
                                                                viewModel.saveSecurityAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                                                    if (it) {
                                                                        viewModel.saveLightAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                                                            if (it) {
                                                                                viewModel.saveOtherAmenities(mListener?.getDhabaModelMain()!!, GenericCallBack {
                                                                                    if (it) {
                                                                                        viewModel.saveBankDetails(mListener?.getDhabaModelMain()!!.bankDetailsModel, GenericCallBack {
                                                                                            if (it != null) {
                                                                                                mListener?.getDhabaModelMain()?.dhabaModel?.let {
                                                                                                    // UPDATING DHABA STATUS TO ISDRAFT
                                                                                                    updateDhabaStatus(isDraft)
                                                                                                } ?: kotlin.run {
                                                                                                    showToastInCenter(getString(R.string.enter_dhaba_details_first))
                                                                                                    mListener?.showDhabaScreen()
                                                                                                }
                                                                                            }
                                                                                        })
                                                                                    }
                                                                                })
                                                                            }
                                                                        })
                                                                    }
                                                                })
                                                            }
                                                        })
                                                    }
                                                })
                                            }
                                        })
                                    }
                                })
                            }
                        })
                    }
                })
            }
        })
    }
}