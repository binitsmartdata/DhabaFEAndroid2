package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentSleepingAmenitiesBinding
import com.transport.mall.model.SleepingAmenitiesModel
import com.transport.mall.ui.addnewdhaba.step3.foodamenities.SleepingAmenitiesVM
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class SleepingAmenitiesFragment :
    BaseFragment<FragmentSleepingAmenitiesBinding, SleepingAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_sleeping_amenities
    override var viewModel: SleepingAmenitiesVM
        get() = setUpVM(this, SleepingAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentSleepingAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        mListener = activity as AddDhabaListener
        binding.context = activity
        binding.lifecycleOwner = this
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        setupLicensePhotoViews()
        setupFoodPhotosView()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: SleepingAmenitiesModel) {
        it.sleeping.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbBedCharpaiYes.isChecked = true
            } else {
                binding.rbBedCharpaiNo.isChecked = true
            }
        }
        it.noOfBeds.let {
            when (it) {
                "1" -> binding.rbBed1.isChecked = true
                "2" -> binding.rbBed5.isChecked = true
                "3" -> binding.rbBedAbove5.isChecked = true
            }
        }
        it.images.let {
            if (it.isNotEmpty()) {
                xloadImages(
                    binding.ivSleepingImg,
                    it,
                    R.drawable.ic_placeholder_outliner
                )
                binding.ivSleepingImg.visibility = View.VISIBLE
            }
        }

        it.fan.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbFanYes.isChecked = true
            } else {
                binding.rbFanNo.isChecked = true
            }
        }
        it.cooler.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbCoolerYes.isChecked = true
            } else {
                binding.rbCoolerNo.isChecked = true
            }
        }
        it.enclosed.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbExposedYes.isChecked = true
            } else {
                binding.rbExposedNo.isChecked = true
            }
        }
        it.open.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbOpenYes.isChecked = true
            } else {
                binding.rbOpenNo.isChecked = true
            }
        }
        it.hotWater.let {
            val value = it.toBoolean()
            if (value) {
                binding.rbHotWaterYes.isChecked = true
            } else {
                binding.rbHotWaterNo.isChecked = true
            }
        }

        binding.btnSaveDhaba.visibility = View.GONE
    }

    private fun setupLicensePhotoViews() {
        binding.llSleepingAmanPhoto.setOnClickListener {
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
        binding.llSleepingAmanPhoto.setOnClickListener {
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
        binding.rgBedCharpai.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbBedCharpaiYes -> viewModel.model.sleeping = "true"
                R.id.rbBedCharpaiNo -> viewModel.model.sleeping = "false"
            }
        }

        binding.rgNoOfCharpai.setOnCheckedChangeListener { _, i ->
            viewModel.model.noOfBeds = (activity?.findViewById<RadioButton>(i))?.tag.toString()
        }

        binding.rgFan.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbFanYes -> viewModel.model.fan = "true"
                R.id.rbFanNo -> viewModel.model.fan = "false"
            }
        }

        binding.rgCooler.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbCoolerYes -> viewModel.model.cooler = "true"
                R.id.rbCoolerNo -> viewModel.model.cooler = "false"
            }
        }
        binding.rgExposed.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbExposedYes -> viewModel.model.enclosed = "true"
                R.id.rbExposedNo -> viewModel.model.enclosed = "false"
            }
        }
        binding.rgOpen.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbOpenYes -> viewModel.model.open = "true"
                R.id.rbOpenNo -> viewModel.model.open = "false"
            }
        }
        binding.rgHotColdWater.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbHotWaterYes -> viewModel.model.hotWater = "true"
                R.id.rbHotWaterNo -> viewModel.model.hotWater = "false"
            }
        }

        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        viewModel.addSleepingAmenities(GenericCallBack {
                            if (it.data != null) {
                                showToastInCenter(getString(R.string.sleeping_amen_saved))
                                val intent = Intent()
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
            binding.ivSleepingImg.setImageURI(uri)
            binding.ivSleepingImg.visibility = View.VISIBLE
            viewModel.model.images = if (uri.isAbsolute) uri.path!! else getRealPathFromURI(uri)!!
        }
    }
}