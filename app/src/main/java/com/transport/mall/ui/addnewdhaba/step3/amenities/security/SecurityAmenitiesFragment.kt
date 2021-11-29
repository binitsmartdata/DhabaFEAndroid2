package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentSecurityAmenitiesBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.model.SecurityAmenitiesModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class SecurityAmenitiesFragment :
    BaseFragment<FragmentSecurityAmenitiesBinding, SecurityAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_security_amenities
    override var viewModel: SecurityAmenitiesVM
        get() = setUpVM(this, SecurityAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentSecurityAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    val INTENT_POL_VERIFICATION = "polVerification"
    val INTENT_IND_CAMERA = "inCamera"
    val INTENT_OUT_CAMERA = "outCamera"
    var IMAGE_INTENT_TYPE = ""
    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity
        binding.viewmodel = viewModel
        binding.viewOnly = mListener?.viewOnly()
//        binding.isUpdate = mListener?.isUpdate()
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        setupLicensePhotoViews()
        refreshIndoorCameraImages()
        refreshOutdoorCameraImages()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.securityAmenitiesModel?.let {
            showData(it)
        }
    }

    private fun showData(it: SecurityAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

        it.dayGuard.let {
            when (it) {
                0 -> binding.rbDayGuard0.isChecked = true
                1 -> binding.rbDayGuard1.isChecked = true
                2 -> binding.rbDayGuard2.isChecked = true
                3 -> binding.rbDayGuard3.isChecked = true
            }
        }
        it.nightGuard.let {
            when (it) {
                0 -> binding.rbNightGuard0.isChecked = true
                1 -> binding.rbNightGuard1.isChecked = true
                2 -> binding.rbNightGuard2.isChecked = true
                3 -> binding.rbNightGuard3.isChecked = true
            }
        }
        it.policVerification.let {
            when (it) {
                true -> binding.rbPoliceVerificationYes.isChecked = true
                false -> binding.rbPoliceVerificationNo.isChecked = true
            }
        }
        it.verificationImg.let {
            if (it.isNotEmpty()) {
                xloadImages(binding.ivPoliceVerification, it, R.drawable.ic_image_placeholder)
                binding.ivPoliceVerification.visibility = View.VISIBLE
            }
        }
        it.indoorCamera.let {
            when (it) {
                0 -> binding.rbIndoorCameraNo.isChecked = true

                1 -> {
                    binding.rb12IndCam.isChecked = true
                    binding.rbIndoorCameraYes.isChecked = true
                }
                2 -> {
                    binding.rb25IndCam.isChecked = true
                    binding.rbIndoorCameraYes.isChecked = true
                }
                3 -> {
                    binding.rb5aboveIndCam.isChecked = true
                    binding.rbIndoorCameraYes.isChecked = true
                }
            }
        }
        it.indoorCameraImage?.let {
            if (it.isNotEmpty()) {
                refreshIndoorCameraImages()
            }
        }

        it.outdoorCamera.let {
            when (it) {
                0 -> binding.rbOutdoorCameraNo.isChecked = true

                1 -> {
                    binding.rb12OutCam.isChecked = true
                    binding.rbOutdoorCameraYes.isChecked = true
                }
                2 -> {
                    binding.rb25OutCam.isChecked = true
                    binding.rbOutdoorCameraYes.isChecked = true
                }
                3 -> {
                    binding.rb5AboveOutCam.isChecked = true
                    binding.rbOutdoorCameraYes.isChecked = true
                }
            }
        }
        it.outdoorCameraImage?.let {
            if (it.isNotEmpty()) {
                refreshOutdoorCameraImages()
            }
        }
    }

    private fun setupLicensePhotoViews() {
        binding.llLicensePhoto.setOnClickListener {
            if (mListener?.viewOnly()!!) {
                ImagePagerActivity.startForSingle(getmContext(), viewModel.model.verificationImg)
            } else {
                if (viewModel.model.verificationImg.trim().isEmpty()) {
                    launchLicensePicker()
                } else {
                    GlobalUtils.showOptionsDialog(
                        getmContext(),
                        arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                        getString(R.string.choose_action),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    ImagePagerActivity.startForSingle(getmContext(), viewModel.model.verificationImg)
                                }
                                1 -> {
                                    launchLicensePicker()
                                }
                            }
                        })
                }
            }
        }
        binding.frameIndoorCamera.setOnClickListener {
            IMAGE_INTENT_TYPE = INTENT_IND_CAMERA
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        binding.frameOutDoorCamera.setOnClickListener {
            IMAGE_INTENT_TYPE = INTENT_OUT_CAMERA
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }

    private fun launchLicensePicker() {
        IMAGE_INTENT_TYPE = INTENT_POL_VERIFICATION
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun initListeners() {
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        binding.rgDayGuard.setOnCheckedChangeListener { _, i ->
            viewModel.model.dayGuard =
                (activity?.findViewById<RadioButton>(i))?.tag.toString().toInt()
        }
        binding.rgNightGuard.setOnCheckedChangeListener { _, i ->
            viewModel.model.nightGuard =
                (activity?.findViewById<RadioButton>(i))?.tag.toString().toInt()
        }
        binding.rbPoliceVerification.setOnCheckedChangeListener { _, i ->
            viewModel.model.policVerification =
                (activity?.findViewById<RadioButton>(i))?.tag.toString().toBoolean()
        }
        binding.rgIndoorCameras.setOnCheckedChangeListener { _, i ->
            viewModel.model.indoorCamera =
                (activity?.findViewById<RadioButton>(i))?.tag.toString().toInt()
        }
        binding.rgOutdoorCameras.setOnCheckedChangeListener { _, i ->
            viewModel.model.outdoorCamera =
                (activity?.findViewById<RadioButton>(i))?.tag.toString().toInt()
        }
        binding.rgIndoorCameraYesNo.setOnCheckedChangeListener { _, i ->
            viewModel.model.indoorCameraEnabled = binding.rbIndoorCameraYes.isChecked
            if (!binding.rbIndoorCameraYes.isChecked) {
                viewModel.model.indoorCamera = 0 // set default value
                viewModel.model.indoorCameraImage = ArrayList()
                refreshIndoorCameraImages()
            }
        }
        binding.rgOutdoorCameraYesNo.setOnCheckedChangeListener { _, i ->
            viewModel.model.outdoorCameraEnabled = binding.rbOutdoorCameraYes.isChecked
            if (!binding.rbOutdoorCameraYes.isChecked) {
                viewModel.model.outdoorCamera = 0// set default value
                viewModel.model.outdoorCameraImage = ArrayList()
                refreshOutdoorCameraImages()
            }
        }
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        if (viewModel.model._id.isNotEmpty()) {
                            viewModel.updateSecurityAmenities(GenericCallBack {
                                handleData(it)
                            })
                        } else {
                            viewModel.addSecurityAmenities(GenericCallBack {
                                handleData(it)
                            })
                        }
                    } else {
                        showToastInCenter(message)
                    }
                })
        }
    }

    private fun handleData(it: ApiResponseModel<SecurityAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.security_amen_saved))
            val intent = Intent()
            intent.putExtra("data", it.data)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            showToastInCenter(it.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            when (IMAGE_INTENT_TYPE) {
                INTENT_POL_VERIFICATION -> {
                    // Use Uri object instead of File to avoid storage permissions
                    binding.ivPoliceVerification.setImageURI(uri)
                    binding.ivPoliceVerification.visibility = View.VISIBLE
                    viewModel.model.verificationImg = getRealPathFromURI(uri)
                }
                INTENT_IND_CAMERA -> {
                    viewModel.model.indoorCameraImage.add(
                        PhotosModel("", getRealPathFromURI(uri))
                    )
                    refreshIndoorCameraImages()
                }
                INTENT_OUT_CAMERA -> {
                    viewModel.model.outdoorCameraImage.add(
                        PhotosModel("", getRealPathFromURI(uri))
                    )
                    refreshOutdoorCameraImages()
                }
            }
        }
    }

    private fun refreshIndoorCameraImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerViewIndoorCameras.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        val adapter = ImageGalleryAdapter(
            activity as Context, mListener?.viewOnly(),
            viewModel.model.indoorCameraImage,
            GenericCallBack { })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delSecurityImg(it, null, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })
        binding.recyclerViewIndoorCameras.adapter = adapter

        binding.recyclerViewIndoorCameras.setHasFixedSize(true)
    }

    private fun refreshOutdoorCameraImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerViewOutdoorCameras.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        val adapter = ImageGalleryAdapter(
            activity as Context, mListener?.viewOnly(),
            viewModel.model.outdoorCameraImage,
            GenericCallBack { })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delSecurityImg(null, it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })
        binding.recyclerViewOutdoorCameras.adapter = adapter

        binding.recyclerViewOutdoorCameras.setHasFixedSize(true)
    }

}