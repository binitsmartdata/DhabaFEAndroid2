package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAddDhabaStep2Binding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AddDhabaStep2Fragment :
    BaseFragment<FragmentAddDhabaStep2Binding, AddDhabaStep2VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step2
    override var viewModel: AddDhabaStep2VM
        get() = setUpVM(this, AddDhabaStep2VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep2Binding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    private val PICKER_OWNER_IMAGE = 1
    private val PICKER_ID_FRONT = 2
    private val PICKER_ID_BACK = 3
    private var INTENT_TYPE = 0

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
    }

    override fun initListeners() {
        mListener?.getDhabaModelMain()?.dhabaModel?.let { viewModel.dhaba_id.set(it._id) }
        binding.edPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.uploadPictureLayout.setOnClickListener {
            INTENT_TYPE = PICKER_OWNER_IMAGE
            ImagePicker.with(this)
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.uploadFrontSideLayout.setOnClickListener {
            INTENT_TYPE = PICKER_ID_FRONT
            launchImagePicker()
        }
        binding.uploadBackSideLayout.setOnClickListener {
            INTENT_TYPE = PICKER_ID_BACK
            launchImagePicker()
        }
        binding.btnNext.setOnClickListener {
            viewModel.ownerModel.hasEverything(GenericCallBackTwoParams { hasEverything, message ->
                if (hasEverything) {
                    viewModel.addDhabaOwner(GenericCallBack {
                        mListener?.getDhabaModelMain()?.ownerModel = it.data
                        mListener?.showNextScreen()
                    })
                } else {
                    showToastInCenter(message)
                }
            })
        }
    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop()
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
            when (INTENT_TYPE) {
                PICKER_OWNER_IMAGE -> {
                    binding.ivOwnerImage.setImageURI(uri)
                    viewModel.ownerPic.set(if (uri.isAbsolute) uri.path else getRealPathFromURI(uri))
                }
                PICKER_ID_FRONT -> {
                    binding.ivFrontId.setImageURI(uri)
                    viewModel.idproofFront.set(
                        if (uri.isAbsolute) uri.path else getRealPathFromURI(
                            uri
                        )
                    )
                }
                PICKER_ID_BACK -> {
                    binding.ivBackId.setImageURI(uri)
                    viewModel.idproofBack.set(
                        if (uri.isAbsolute) uri.path else getRealPathFromURI(
                            uri
                        )
                    )
                }
            }
        }
    }

    fun youAreInFocus() {

    }
}