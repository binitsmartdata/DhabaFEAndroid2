package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentBankDetailsBinding
import com.transport.mall.model.*
import com.transport.mall.ui.addnewdhaba.step4.*
import com.transport.mall.ui.customdialogs.ConfirmationDialog
import com.transport.mall.ui.customdialogs.DialogDropdownOptions
import com.transport.mall.ui.home.CommonActivity
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class BankDetailsFragment :
    BaseFragment<FragmentBankDetailsBinding, BankDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_bank_details
    override var viewModel: BankDetailsVM
        get() = setUpVM(activity as AppCompatActivity, BankDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentBankDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null
    var bankList: ArrayList<BankNamesModel> = ArrayList()
    lateinit var banksAdapter: ArrayAdapter<BankNamesModel>

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
        binding.isUpdate = mListener?.isUpdate()
        binding.viewOnly = mListener?.viewOnly()
        binding.dhabaModelMain = mListener?.getDhabaModelMain()
        binding.currentDate = GlobalUtils.getCurrentDate()
        binding.userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()

        //SETTING EXISTING DATA ON SCREEN
        showDataIfHas()

        // getexecutive, manager roles and show(read only)
/*
        viewModel.getUserByRole(GenericCallBack {
            it.data?.let {
                binding.executiveModel = it
            }
        })
*/

        /*if (isExecutiveReviewingOwnerDhaba()) {
            binding.btnSaveDraft.visibility = View.INVISIBLE
        }*/
    }

    private fun showDataIfHas() {
        mListener?.getDhabaModelMain()?.bankDetailsModel?.let {
            viewModel.bankModel = it
            it.panPhoto.let {
                if (it.isNotEmpty()) {
                    xloadImages(binding.ivPanPhoto, it, R.drawable.ic_image_placeholder)
                    binding.ivPanPhoto.visibility = View.VISIBLE
                }
            }
        }
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.dhabaModel = it
            setBlockingData(it)
        }
    }

    private fun setBlockingData(it: DhabaModel) {
        viewModel.dhabaModel.blockDay.let {
            when (it) {
                15 -> binding.rb15Days.isChecked = true
                30 -> binding.rb30Days.isChecked = true
                0 -> binding.rbDelisted.isChecked = true
            }
        }
        viewModel.dhabaModel.active?.let {
            when (it) {
                "true" -> binding.rbActive.isChecked = true
                "false" -> binding.rbInactive.isChecked = true
            }
        }
        viewModel.dhabaModel.blockMonth.let {
            viewModel.blockingMonths.set(it.toString())
        }
    }

    override fun initListeners() {
        setupBankNameDropdown()

        binding.rgBlockingFor.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.dhabaModel.blockDay =
                (activity?.findViewById<RadioButton>(i))?.getTag().toString().toInt()
        }
        binding.rgPropertyStatus.setOnCheckedChangeListener { radioGroup, i ->
//            viewModel.dhabaModel.status =
//                activity?.findViewById<RadioButton>(i)?.getTag().toString()
        }

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.btnNext.setOnClickListener {
            GlobalUtils.disableTemporarily(it)
            viewModel.dhabaModel.isDraft = false.toString()
            saveDetails(false)
        }
        binding.btnSaveDraft.setOnClickListener {
            GlobalUtils.disableTemporarily(it)
            ConfirmationDialog(
                getmContext(),
                getString(R.string.are_you_sure_you_want_to_save_as_draft),
                {
                    if (it) {
                        viewModel.dhabaModel.isDraft = true.toString()
                        /*if (mListener?.getDhabaModelMain()?.bankDetailsModel != null) {
                            mListener?.getDhabaModelMain()?.draftedAtScreen =
                                DhabaModelMain.DraftScreen.BankDetailsFragment.toString()
                            mListener?.saveAsDraft()
                            activity?.finish()
                        } else {*/
                        saveDetails(true)
//            }
                    }
                }).show()
        }

        binding.llPanPhoto.setOnClickListener {
            launchImagePicker()
        }
        setRxBusListener()
    }

    private fun setRxBusListener() {
        //LISTENER TO LISTEN WHEN TO EXECUTE SAVE BUTTON
        RxBus.listen(DhabaModelMain.ActiveScreen::class.java).subscribe {
            if (it == DhabaModelMain.ActiveScreen.BankDetailsFragment) {
                try {
                    binding.btnNext.performClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupBankNameDropdown() {
        AppDatabase.getInstance(getmContext())?.bankDao()
            ?.getAll()?.observe(this, Observer {
                bankList = it as ArrayList<BankNamesModel>
                if (bankList.isNotEmpty()) {
                    setBankNamesAdapter(bankList)
                }
            })
    }

    private fun setBankNamesAdapter(bankList: java.util.ArrayList<BankNamesModel>) {
        banksAdapter =
            ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, bankList)
        binding.edBank.setOnClickListener {
            DialogDropdownOptions(getmContext(), getString(R.string.select_bank), banksAdapter, {
                viewModel.bankModel.bankName = bankList[it].name!!
            }).show()
        }
    }

    private fun saveDetails(isDraft: Boolean) {
//        if (isDraft || (!isDraft && isHavingPreviousData())) {
        if (isHavingPreviousData()) {
            if (isDraft) {
//                if (viewModel.bankModel.bankName.trim().isNotEmpty()) {
                proceed(isDraft)
//                } else {
//                    showToastInCenter(getString(R.string.enter_bank_name))
//                }
            } else {
                var ownerMissingParams = ""
                var dhabaMissingParams = ""
                mListener?.getDhabaModelMain()?.ownerModel?.let {
                    ownerMissingParams = it.getMissingParameters(getmContext())?.trim()!!
                }
                mListener?.getDhabaModelMain()?.dhabaModel?.let {
                    dhabaMissingParams = it.getMissingParameters(getmContext())?.trim()!!
                }
                if (ownerMissingParams.isNotEmpty() || dhabaMissingParams.isNotEmpty()) {
                    GlobalUtils.showInfoDialog(getmContext(),
                        getString(R.string.details_missing_validation),
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
                    viewModel.bankModel.hasEverything(
                        getmContext(),
                        GenericCallBackTwoParams() { allOk, message ->
                            if (allOk) {
                                proceed(isDraft)
                            } else {
                                showToastInCenter(message)
                            }
                        })
                }
            }
        }
    }

    private fun proceed(isDraft: Boolean) {
        saveOwnerDetails(GenericCallBack {
            if (it) {
                saveDhabaDetails(GenericCallBack {
                    if (it) {
                        saveFoodAmenities(GenericCallBack {
                            if (it) {
                                saveParkingAmenities(GenericCallBack {
                                    if (it) {
                                        saveSleepingAmenities(GenericCallBack {
                                            if (it) {
                                                saveWashroomAmenities(GenericCallBack {
                                                    if (it) {
                                                        saveSecurityAmenities(GenericCallBack {
                                                            if (it) {
                                                                saveLightAmenities(GenericCallBack {
                                                                    if (it) {
                                                                        saveOtherAmenities(
                                                                            GenericCallBack {
                                                                                saveBankDetails(
                                                                                    isDraft
                                                                                )
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

    private fun saveFoodAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateFoodAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addFoodAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })

            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveParkingAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateParkingAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addParkingAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveSleepingAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateSleepingAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addSleepingAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveWashroomAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updatewashroomAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addWashroomAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveSecurityAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.securityAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateSecurityAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addSecurityAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveLightAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateLightAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addLightAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveOtherAmenities(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.otherAmenitiesModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateOtherAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addOtherAmenities(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        } ?: kotlin.run {
            callBack.onResponse(true)
        }
    }

    private fun saveDhabaDetails(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateDhaba(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addDhaba(it, GenericCallBack {
                    if (it.data != null) {
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })

            }
        }
    }

    private fun saveOwnerDetails(callBack: GenericCallBack<Boolean>) {
        mListener?.getDhabaModelMain()?.ownerModel?.let {
            if (GlobalUtils.getNonNullString(it._id, "").isNotEmpty()) {
                viewModel.updateOwner(it, GenericCallBack {
                    if (it.data != null) {
                        mListener?.getDhabaModelMain()?.dhabaModel?.owner_id = it?.data?._id!!
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            } else {
                viewModel.addDhabaOwner(it, GenericCallBack {
                    if (it.data != null) {
                        mListener?.getDhabaModelMain()?.dhabaModel?.owner_id = it?.data?._id!!
                        callBack.onResponse(true)
                    } else {
                        callBack.onResponse(false)
                        showToastInCenter(it.message)
                    }
                })
            }
        }
    }

    private fun saveBankDetails(isDraft: Boolean) {
        if (viewModel.bankModel._id.isNotEmpty()) {
            viewModel.updateBankDetail(GenericCallBack {
                handleData(it, isDraft)
            })
        } else {
            viewModel.addBankDetail(GenericCallBack {
                handleData(it, isDraft)
            })
        }
    }

    private fun handleData(it: ApiResponseModel<BankDetailsModel>, isDraft: Boolean) {
        if (it.data != null) {
            mListener?.getDhabaModelMain()?.bankDetailsModel = it.data
            viewModel.bankModel = it.data!!

            viewModel.updateDhabaStatus(
                activity as Context,
                isDraft,
                viewModel.dhabaModel,
                if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS,
                !isDraft,
                viewModel.progressObserver,
                GenericCallBack {
                    if (it.data != null) {
                        mListener?.getDhabaModelMain()?.dhabaModel = it.data
                        if (isDraft) {
                            mListener?.getDhabaModelMain()?.draftedAtScreen =
                                DhabaModelMain.DraftScreen.BankDetailsFragment.toString()
                            mListener?.saveAsDraft()
                            activity?.finish()
                        } else {
                            showSuccessDialog(mListener?.getDhabaModelMain()?.dhabaModel?._id!!)
                        }
                    } else {
                        showToastInCenter(it.message)
                    }
                })
        } else {
            showToastInCenter(it.message)
        }
    }

    fun showSuccessDialog(dhabaId: String) {
        CommonActivity.showDhabaSuccessMessage(
            getmContext(),
            CommonActivity.TYPE_DHABA_SUCCESS,
            dhabaId
        )
        activity?.finish()
    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop(16f, 9f)
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            binding.ivPanPhoto.setImageURI(uri)
            binding.ivPanPhoto.visibility = View.VISIBLE
            viewModel.bankModel.panPhoto = getRealPathFromURI(uri)
        }
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
        /*mListener?.getDhabaModelMain()?.ownerModel?.let {
            viewModel.bankModel.panNumber = it.panNumber
        }*/
        mListener?.getDhabaModelMain()?.ownerModel?.let { viewModel.bankModel.user_id = it._id }
        mListener?.getDhabaModelMain()?.dhabaModel?.let { viewModel.dhabaModel = it }
    }

    fun isExecutiveReviewingOwnerDhaba(): Boolean {
        if (mListener?.isUpdate()!! && SharedPrefsHelper.getInstance(getmContext()).getUserData()
                .isExecutive()
        ) {
            return mListener?.getDhabaModelMain()?.dhabaModel?.approval_for.equals(UserModel.ROLE_EXECUTIVE)
        } else {
            return false
        }
    }
}