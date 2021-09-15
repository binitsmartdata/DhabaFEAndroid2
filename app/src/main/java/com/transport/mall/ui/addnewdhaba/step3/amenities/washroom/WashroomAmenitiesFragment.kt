package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentWashroomAmenitiesBinding
import com.transport.mall.model.WashroomAmenitiesModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class WashroomAmenitiesFragment :
    BaseFragment<FragmentWashroomAmenitiesBinding, WashroomAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_washroom_amenities
    override var viewModel: WashroomAmenitiesVM
        get() = setUpVM(this, WashroomAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentWashroomAmenitiesBinding
        get() = setUpBinding()
        set(value) {}
    var mListener: AddDhabaListener? = null

    override fun bindData() {
        mListener = activity as AddDhabaListener
        binding.context = activity
        setupLicensePhotoViews()
        setupFoodPhotosView()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: WashroomAmenitiesModel) {
        it.washroomStatus.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbOpenYes.isChecked = true
            } else {
                binding.rbOpenNo.isChecked = true
            }
        }

        it.water.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbHotWaterYes.isChecked = true
            } else {
                binding.rbHotWaterNo.isChecked = true
            }
        }

        it.cleaner.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbCleanerYes.isChecked = true
            } else {
                binding.rbCleanerNo.isChecked = true
            }
        }

        it.images.let {
            if (it.isNotEmpty()) {
                xloadImages(
                    binding.ivWashroomPhoto,
                    it,
                    R.drawable.ic_placeholder_outliner
                )
                binding.ivWashroomPhoto.visibility = View.VISIBLE
            }
        }

        binding.btnSaveDhaba.visibility = View.GONE
    }

    private fun setupLicensePhotoViews() {
        binding.llWashroomPhoto.setOnClickListener {
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
        binding.llWashroomPhoto.setOnClickListener {
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

        binding.rgOpen.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbOpenYes -> viewModel.model.washroomStatus = "true"
                R.id.rbOpenNo -> viewModel.model.washroomStatus = "false"
            }
        }
        binding.rgHotColdWater.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbHotWaterYes -> viewModel.model.water = "true"
                R.id.rbHotWaterNo -> viewModel.model.water = "false"
            }
        }
        binding.rgCleaner.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbCleanerYes -> viewModel.model.cleaner = "true"
                R.id.rbCleanerNo -> viewModel.model.cleaner = "false"
            }
        }

        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        viewModel.addWashroomAmenities(GenericCallBack {
                            if (it.data != null) {
                                showToastInCenter(getString(R.string.sleeping_amen_saved))
                                var intent = Intent()
                                intent.putExtra("data", it.data)
                                activity?.setResult(Activity.RESULT_OK, intent)
                                activity?.finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.ivWashroomPhoto.setImageURI(uri)
            binding.ivWashroomPhoto.visibility = View.VISIBLE
            viewModel.model.images = if (uri.isAbsolute) uri.path!! else getRealPathFromURI(uri)!!
        }
    }
}