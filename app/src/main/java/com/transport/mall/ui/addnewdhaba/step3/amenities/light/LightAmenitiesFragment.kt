package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentLightAmenitiesBinding
import com.transport.mall.model.LightAmenitiesModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class LightAmenitiesFragment :
    BaseFragment<FragmentLightAmenitiesBinding, LightAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_light_amenities
    override var viewModel: LightAmenitiesVM
        get() = setUpVM(this, LightAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentLightAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    val INTENT_TOWER = "tower"
    val INTENT_BULB = "bulb"
    var INTENT_TYPE = ""

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity
//        binding.isUpdate = mListener?.isUpdate()
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }
        setupLicensePhotoViews()
        setupFoodPhotosView()

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            showData(it)
        }
    }

    private fun showData(it: LightAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

        it.towerLight.let {
            when (it) {
                true -> binding.rbTowerYes.isChecked = true
                false -> binding.rbTowerNo.isChecked = true
            }
        }
        it.bulbLight.let {
            when (it) {
                true -> binding.rbBulbYes.isChecked = true
                false -> binding.rbBulbNo.isChecked = true
            }
        }
        it.electricityBackup.let {
            when (it) {
                true -> binding.rbElectricityYes.isChecked = true
                false -> binding.rbElectricityNo.isChecked = true
            }
        }
        it.towerLightImage.let {
            if (it.isNotEmpty()) {
                xloadImages(binding.ivTowerImg, it, R.drawable.ic_image_placeholder)
                binding.ivTowerImg.visibility = View.VISIBLE
            }
        }
        it.bulbLightImage.let {
            if (it.isNotEmpty()) {
                xloadImages(binding.ivBulbImage, it, R.drawable.ic_image_placeholder)
                binding.ivBulbImage.visibility = View.VISIBLE
            }
        }
    }

    private fun setupLicensePhotoViews() {
        binding.frameTowerPhoto.setOnClickListener {
            INTENT_TYPE = INTENT_TOWER
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

    private fun setupFoodPhotosView() {
        binding.frameBulbPhoto.setOnClickListener {
            INTENT_TYPE = INTENT_BULB
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

    override fun initListeners() {
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        binding.rgTower.setOnCheckedChangeListener { _, i ->
            viewModel.model.towerLight = binding.rbTowerYes.isChecked
        }
        binding.rgBulb.setOnCheckedChangeListener { _, i ->
            viewModel.model.bulbLight = binding.rbBulbYes.isChecked
        }
        binding.rgElectricity.setOnCheckedChangeListener { _, i ->
            viewModel.model.electricityBackup = binding.rbElectricityYes.isChecked
        }
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        if (viewModel.model._id.isNotEmpty()) {
                            viewModel.updateLightAmenities(GenericCallBack {
                                handleData(it)
                            })
                        } else {
                            viewModel.addLightAmenities(GenericCallBack {
                                handleData(it)
                            })
                        }
                    } else {
                        showToastInCenter(message)
                    }
                })
        }
    }

    private fun handleData(it: ApiResponseModel<LightAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.amen_saved))
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

            // Use Uri object instead of File to avoid storage permissions
            when (INTENT_TYPE) {
                INTENT_TOWER -> {
                    binding.ivTowerImg.setImageURI(uri)
                    binding.ivTowerImg.visibility = View.VISIBLE
                    viewModel.model.towerLightImage = getRealPathFromURI(uri)
                }
                INTENT_BULB -> {
                    binding.ivBulbImage.setImageURI(uri)
                    binding.ivBulbImage.visibility = View.VISIBLE
                    viewModel.model.bulbLightImage = getRealPathFromURI(uri)
                }
            }
        }
    }
}