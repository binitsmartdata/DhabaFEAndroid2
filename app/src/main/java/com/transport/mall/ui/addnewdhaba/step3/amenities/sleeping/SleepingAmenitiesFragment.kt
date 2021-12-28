package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentSleepingAmenitiesBinding
import com.transport.mall.model.SleepingAmenitiesModel
import com.transport.mall.ui.addnewdhaba.step3.foodamenities.SleepingAmenitiesVM
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
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
        binding.viewOnly = mListener?.viewOnly()
//        binding.isUpdate = mListener?.isUpdate()!!
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        setupFoodPhotosView()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.sleepingAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: SleepingAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

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
                    R.drawable.ic_image_placeholder
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
        it.enclosed?.let {
            when (it) {
                "1" -> binding.rbExposedIndoor.isChecked = true
                "2" -> binding.rbExposedOutdoor.isChecked = true
                "3" -> binding.rbExposedBoth.isChecked = true
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
    }

    private fun setupFoodPhotosView() {
        binding.llSleepingAmanPhoto.setOnClickListener {
            if (mListener?.viewOnly()!!) {
                ImagePagerActivity.startForSingle(getmContext(), viewModel.model.images)
            } else {
                if (viewModel.model.images.trim().isEmpty()) {
                    launchSleepingAmenitiesImgPicker()
                } else {
                    GlobalUtils.showOptionsDialog(
                        getmContext(),
                        arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                        getString(R.string.choose_action),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    ImagePagerActivity.startForSingle(getmContext(), viewModel.model.images)
                                }
                                1 -> {
                                    launchSleepingAmenitiesImgPicker()
                                }
                            }
                        })
                }
            }
        }
    }

    private fun launchSleepingAmenitiesImgPicker() {
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
        binding.rgExposed.setOnCheckedChangeListener { _, id ->
            viewModel.model.enclosed = activity?.findViewById<RadioButton>(id)?.getTag().toString()
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
                        /*if (viewModel.model._id.isNotEmpty()) {
                            viewModel.updateSleepingAmenities(GenericCallBack {
                                handleResponse(it)
                            })
                        } else {
                            viewModel.addSleepingAmenities(GenericCallBack {
                                handleResponse(it)
                            })
                        }*/
                        val intent = Intent()
                        intent.putExtra("data", viewModel.model)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    } else {
                        showToastInCenter(message)
                    }
                })
        }
    }

    private fun handleResponse(it: ApiResponseModel<SleepingAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.sleeping_amen_saved))
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
            binding.ivSleepingImg.setImageURI(uri)
            binding.ivSleepingImg.visibility = View.VISIBLE
            viewModel.model.images = getRealPathFromURI(uri)
        }
    }
}