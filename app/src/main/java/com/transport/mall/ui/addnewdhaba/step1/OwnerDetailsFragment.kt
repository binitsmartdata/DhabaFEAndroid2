package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentOwnerDetailsBinding
import com.transport.mall.model.DhabaOwnerModel
import com.transport.mall.model.LocationAddressModel
import com.transport.mall.ui.addnewdhaba.GoogleMapsActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OwnerDetailsFragment :
    BaseFragment<FragmentOwnerDetailsBinding, OwnerDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_owner_details
    override var viewModel: OwnerDetailsVM
        get() = setUpVM(this, OwnerDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOwnerDetailsBinding
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

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.ownerModel?.let {
            viewModel.ownerModel = it
            setData(it)
        }

        binding.btnNext.isEnabled = !mListener?.isUpdate()!!
        binding.btnSaveDraft.isEnabled = !mListener?.isUpdate()!!
        binding.viewRestrictor.visibility =
            if (!mListener?.isUpdate()!!) View.VISIBLE else View.GONE

        binding.isUpdate = mListener?.isUpdate()!!
    }

    private fun setData(it: DhabaOwnerModel) {
        it.ownerPic.let {
            xloadImages(binding.ivOwnerImage, it, R.drawable.ic_profile_pic_placeholder)
        }
        it.idproofFront.let {
            if (it.isNotEmpty()) {
                xloadImages(binding.ivFrontId, it, R.drawable.ic_image_placeholder)
            }
        }
        it.idproofBack.let {
            if (it.isNotEmpty()) {
                xloadImages(binding.ivBackId, it, R.drawable.ic_image_placeholder)
            }
        }
    }

    override fun initListeners() {
        setupLocationViews()
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
            if (mListener?.getDhabaModelMain()?.ownerModel != null) {
                mListener?.showNextScreen()
            } else {
                saveDetails(false)
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            if (mListener?.getDhabaModelMain()?.ownerModel != null) {
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                saveDetails(true)
            }
        }
    }

    private fun setupLocationViews() {
        binding.tvMapPicker.setOnClickListener {
            GoogleMapsActivity.start(this)
        }
        binding.tvCurrLocation.setOnClickListener {
            GlobalUtils.getCurrentLocation(activity as Context, GenericCallBack { location ->
                if (location != null) {
                    viewModel.ownerModel.address = GlobalUtils.getAddressUsingLatLong(
                        activity as Context,
                        location.latitude,
                        location.longitude
                    ).fullAddress!!
                }
            })
        }
    }

    private fun saveDetails(isDraft: Boolean) {
        viewModel.ownerModel.hasEverything(GenericCallBackTwoParams { hasEverything, message ->
            if (hasEverything) {
                viewModel.addDhabaOwner(GenericCallBack {
                    mListener?.getDhabaModelMain()?.ownerModel = it.data
                    if (isDraft) {
                        mListener?.saveAsDraft()
                        activity?.finish()
                    } else {
                        showToastInCenter(getString(R.string.owner_saved))
                        mListener?.showNextScreen()
                    }
                })
            } else {
                showToastInCenter(message)
            }
        })
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
            if (requestCode == GoogleMapsActivity.REQUEST_CODE_MAP) {
                val location = data?.getSerializableExtra("data") as LocationAddressModel?
                location.let {
                    viewModel.ownerModel.address = it?.fullAddress!!
                    viewModel.ownerModel.latitude = it.latitude.toString()
                    viewModel.ownerModel.longitude = it.longitude.toString()
                }
            } else {
                val uri: Uri = data?.data!!
                when (INTENT_TYPE) {
                    PICKER_OWNER_IMAGE -> {
                        binding.ivOwnerImage.setImageURI(uri)
                        viewModel.ownerModel.ownerPic = getRealPathFromURI(uri)
                    }
                    PICKER_ID_FRONT -> {
                        binding.ivFrontId.setImageURI(uri)
                        viewModel.ownerModel.idproofFront = getRealPathFromURI(uri)
                    }
                    PICKER_ID_BACK -> {
                        binding.ivBackId.setImageURI(uri)
                        viewModel.ownerModel.idproofBack = getRealPathFromURI(uri)
                    }
                }
            }
        }
    }

    fun youAreInFocus() {

    }
}