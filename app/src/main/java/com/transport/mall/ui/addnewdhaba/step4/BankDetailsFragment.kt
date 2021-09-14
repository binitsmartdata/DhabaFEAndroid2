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
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

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
            viewModel.bankModel.hasEverything(GenericCallBackTwoParams() { allOk, message ->
                if (allOk) {
                    viewModel.addBankDetail(GenericCallBack {
                        if (it.data != null) {
                            mListener?.getDhabaModelMain()?.bankDetailsModel = it.data
                            showSuccessDialog(mListener?.getDhabaModelMain()?.dhabaModel?._id!!)
                        } else {
                            showToastInCenter(it.message)
                        }
                    })
                } else {
                    showToastInCenter(message)
                }
            })
        }

        binding.llPanPhoto.setOnClickListener {
            launchImagePicker()
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

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.panNumber.set(it?.panNumber) }
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.user_id.set(it?._id) }
    }
}