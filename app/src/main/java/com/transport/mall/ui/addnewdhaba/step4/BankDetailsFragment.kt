package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentStep4BankDetailsBinding
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
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
    BaseFragment<FragmentStep4BankDetailsBinding, BankDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_step4_bank_details
    override var viewModel: BankDetailsVM
        get() = setUpVM(this, BankDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentStep4BankDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.panNumber.set(it?.ownerName) }
        binding.executiveModel = SharedPrefsHelper.getInstance(activity as Context).getUserData()
        binding.dhabaModelMain = mListener?.getDhabaModelMain()
        binding.currentDate = GlobalUtils.getCurrentDate()

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.bankDetailsModel?.let {
            setData(it)
        }
    }

    private fun setData(it: BankDetailsModel) {
        it.bankName.let {
            viewModel.bankName.set(it)
        }
        it.gstNumber.let {
            viewModel.gstNumber.set(it)
        }
        it.ifscCode.let {
            viewModel.ifscCode.set(it)
        }
        it.accountName.let {
            viewModel.accountName.set(it)
        }
        it.panNumber.let {
            viewModel.panNumber.set(it)
        }
        it.panPhoto.let {
            viewModel.panPhoto.set(it)
            xloadImages(binding.ivPanPhoto, it, R.drawable.ic_placeholder_outliner)
            binding.ivPanPhoto.visibility = View.VISIBLE
        }
    }

    override fun initListeners() {
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.btnNext.setOnClickListener {
            if (mListener?.getDhabaModelMain()?.bankDetailsModel != null) {
                showSuccessDialog(mListener?.getDhabaModelMain()?.dhabaModel?._id!!)
            } else {
                saveDetails(false)
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            saveDetails(true)
        }

        binding.llPanPhoto.setOnClickListener {
            launchImagePicker()
        }
    }

    private fun saveDetails(isDraft: Boolean) {
        if (isDraft || (!isDraft && isHavingPreviousData())) {
            viewModel.bankModel.hasEverything(GenericCallBackTwoParams() { allOk, message ->
                if (allOk) {
                    viewModel.addBankDetail(GenericCallBack {
                        if (it.data != null) {
                            mListener?.getDhabaModelMain()?.bankDetailsModel = it.data
                            if (isDraft) {
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
                    showToastInCenter(message)
                }
            })
        }
    }

    fun showSuccessDialog(dhabaId: String) {
        DialogAddDhabaSuccess(
            activity as Context,
            dhabaId,
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

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop(74f, 52f)
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
            viewModel.panPhoto.set(
                if (uri.isAbsolute) uri.path else getRealPathFromURI(
                    uri
                )
            )
        }
    }

    private fun isHavingPreviousData(): Boolean {
        if (mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
            return false
        } else if (mListener?.getDhabaModelMain()?.dhabaModel == null) {
            showToastInCenter(getString(R.string.enter_dhaba_details))
            return false
        } /*else if (mListener?.getDhabaModelMain()?.foodAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.parkingAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.sleepingAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.washroomAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.securityAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.lightAmenitiesModel == null
            || mListener?.getDhabaModelMain()?.otherAmenitiesModel == null
        ) {
            showToastInCenter(getString(R.string.fill_amenities_first))
            return false
        }*/
        return true
    }

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.panNumber.set(it?.panNumber) }
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.user_id.set(it?._id) }
    }
}