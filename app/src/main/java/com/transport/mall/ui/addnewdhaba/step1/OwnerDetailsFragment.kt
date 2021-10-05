package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.lifecycle.Observer
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentOwnerDetailsBinding
import com.transport.mall.model.DhabaModelMain
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
        binding.isUpdate = mListener?.isUpdate()!!
        binding.viewOnly = mListener?.viewOnly()!!

        //SETTING EXISTING DATA ON SCREEN
        setDataIfHas()
    }

    private fun setDataIfHas() {
        mListener?.getDhabaModelMain()?.ownerModel?.let {
            viewModel.ownerModel = it

            it.alternativeMobilePrefix?.let {
                if (it.isNotEmpty()) {
                    try {
                        binding.ccpCountryCodeAlt.setCountryForPhoneCode(it.toInt())
                    } catch (e: Exception) {
                        binding.ccpCountryCodeAlt.setCountryForPhoneCode(91)
                    }
                }
            }
            it.mobilePrefix?.let {
                if (it.isNotEmpty()) {
                    try {
                        binding.ccpCountryCode.setCountryForPhoneCode(it.toInt())
                    } catch (e: Exception) {
                        binding.ccpCountryCode.setCountryForPhoneCode(91)
                    }
                }
            }

            it.ownerPic?.let {
                if (it.isNotEmpty()) {
                    xloadImages(binding.ivOwnerImage, it, R.drawable.ic_profile_pic_placeholder)
                }
            }

            it.idproofFront?.let {
                if (it.isNotEmpty()) {
                    xloadImages(binding.ivFrontId, it, R.drawable.ic_transparent_placeholder)
                }
            }
            it.idproofBack?.let {
                if (it.isNotEmpty()) {
                    xloadImages(binding.ivBackId, it, R.drawable.ic_transparent_placeholder)
                }
            }
        } ?: kotlin.run {
            viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
            viewModel.ownerModel.alternativeMobilePrefix = binding.ccpCountryCodeAlt.selectedCountryCode
        }
    }

    override fun initListeners() {
        GlobalUtils.refreshLocation(activity as Context)
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
        }
        binding.ccpCountryCodeAlt.setOnCountryChangeListener {
            viewModel.ownerModel.alternativeMobilePrefix =
                binding.ccpCountryCodeAlt.selectedCountryCode
        }

        setupLocationViews()
        binding.edPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_owner))
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
           /* if (mListener?.getDhabaModelMain()?.ownerModel != null && !mListener?.isUpdate()!!) {
                mListener?.showNextScreen()
            } else {*/
                saveDetails(false)
//            }
        }
        binding.btnSaveDraft.setOnClickListener {
            if (mListener?.getDhabaModelMain()?.ownerModel != null) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.OwnerDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                saveDetails(true)
            }
        }
    }

    private fun setupLocationViews() {
        binding.tvMapPicker.setOnClickListener {
            if (GlobalUtils.isLocationEnabled(getmContext())) {
                GlobalUtils.selectLocationOnMap(
                    this,
                    viewModel.ownerModel.latitude,
                    viewModel.ownerModel.longitude
                )
            } else {
                GlobalUtils.showConfirmationDialogYesNo(
                    getmContext(),
                    getString(R.string.location_alert_dialog),
                    GenericCallBack {
                        if (it!!) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } else {
                            GlobalUtils.selectLocationOnMap(
                                this,
                                viewModel.ownerModel.latitude,
                                viewModel.ownerModel.longitude
                            )
                        }
                    })
            }
        }
        binding.tvCurrLocation.setOnClickListener {
            if (GlobalUtils.isLocationEnabled(getmContext())) {
                getAddress()
            } else {
                GlobalUtils.showConfirmationDialogYesNo(
                    getmContext(),
                    getString(R.string.location_alert_dialog),
                    GenericCallBack {
                        if (it!!) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } else {
                            showToastInCenter(getString(R.string.unable_to_fetch_address))
                        }
                    })
            }
        }
    }

    private fun getAddress() {
        GlobalUtils.getCurrentLocation(activity as Context, GenericCallBack { location ->
            if (location != null) {
                0
                viewModel.ownerModel.latitude = location.latitude.toString()
                viewModel.ownerModel.longitude = location.longitude.toString()

                viewModel.ownerModel.address = GlobalUtils.getAddressUsingLatLong(
                    activity as Context,
                    location.latitude,
                    location.longitude
                ).fullAddress!!
            }
        })
    }

    private fun saveDetails(isDraft: Boolean) {
//        viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.textView_selectedCountry.text.toString()
        viewModel.ownerModel.hasEverything(
            getmContext(),
            GenericCallBackTwoParams { hasEverything, message ->
                if (hasEverything) {
                    if (mListener?.isUpdate()!! && viewModel.ownerModel._id.isNotEmpty()) {
                        viewModel.updateOwner(GenericCallBack {
                            handleData(it, isDraft)
                        })
                    } else {
                        viewModel.addDhabaOwner(GenericCallBack {
                            handleData(it, isDraft)
                        })
                    }
                } else {
                    showToastInCenter(message)
                }
            })
    }

    private fun handleData(it: ApiResponseModel<DhabaOwnerModel>, isDraft: Boolean) {
        if (it.data != null) {
            mListener?.getDhabaModelMain()?.ownerModel = it.data
            viewModel.ownerModel = it.data!!

            if (isDraft) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.OwnerDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                if (mListener?.isUpdate()!!) {
                    showToastInCenter(getString(R.string.updated_successfully))
                } else {
                    showToastInCenter(getString(R.string.owner_saved))
                    mListener?.showNextScreen()
                }
            }
        } else {
            showToastInCenter(it.message)
        }
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
            if (requestCode == GoogleMapsActivity.REQUEST_CODE_MAP) {
                val location = data?.getSerializableExtra("data") as LocationAddressModel?
                location.let {
                    viewModel.ownerModel.address = it?.fullAddress!!
                    viewModel.ownerModel.latitude = it.latitude.toString()
                    viewModel.ownerModel.longitude = it.longitude.toString()
                }
            } else if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE) {
                viewModel.ownerModel.address =
                    data?.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS)!!
                viewModel.ownerModel.latitude =
                    data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, -1.toDouble())
                        .toString()
                viewModel.ownerModel.longitude =
                    data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, -1.toDouble())
                        .toString()
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