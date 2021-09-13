package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentSleepingAmenitiesBinding
import com.transport.mall.ui.addnewdhaba.step3.foodamenities.SleepingAmenitiesVM
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams

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

    override fun bindData() {
        binding.context = activity
        setupLicensePhotoViews()
        setupFoodPhotosView()
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
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(GenericCallBackTwoParams { allOk, message ->
                if (allOk) {
                    viewModel.addSleepingAmenities(GenericCallBack {
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
            binding.ivSleepingImg.setImageURI(uri)

        }
    }
}