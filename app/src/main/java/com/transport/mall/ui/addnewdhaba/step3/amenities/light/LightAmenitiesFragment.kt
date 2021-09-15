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
import com.transport.mall.databinding.FragmentLightAmenitiesBinding
import com.transport.mall.model.LightAmenitiesModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.xloadImages
import org.json.JSONObject

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
        setupLicensePhotoViews()
        setupFoodPhotosView()

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.lightAmenitiesModel?.let {
            showData(it)
        }
    }

    private fun showData(it: LightAmenitiesModel) {
        viewModel.model = it
        it.tower_light.let {
            when (it) {
                true -> (activity?.findViewById<RadioButton>(R.id.rbTowerYes))?.isChecked = true
                false -> (activity?.findViewById<RadioButton>(R.id.rbTowerNo))?.isChecked = true
            }
        }
        it.bulb_light.let {
            when (it) {
                true -> (activity?.findViewById<RadioButton>(R.id.rbBulbYes))?.isChecked = true
                false -> (activity?.findViewById<RadioButton>(R.id.rbBulbNo))?.isChecked = true
            }
        }
        it.twentyfour_seven_electricity.let {
            when (it) {
                true -> (activity?.findViewById<RadioButton>(R.id.rbElectricityYes))?.isChecked =
                    true
                false -> (activity?.findViewById<RadioButton>(R.id.rbElectricityNo))?.isChecked =
                    true
            }
        }
        it.tower_image.let {
            xloadImages(binding.ivTowerImg, it, R.drawable.ic_placeholder_outliner)
            binding.ivTowerImg.visibility = View.VISIBLE
        }
        it.bulb_image.let {
            xloadImages(binding.ivBulbImage, it, R.drawable.ic_placeholder_outliner)
            binding.ivBulbImage.visibility = View.VISIBLE
        }

        binding.btnSaveDhaba.visibility = View.GONE
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
            viewModel.model.tower_light =
                (activity?.findViewById<RadioButton>(R.id.rbTowerYes))?.isChecked!!
        }
        binding.rgBulb.setOnCheckedChangeListener { _, i ->
            viewModel.model.bulb_light =
                (activity?.findViewById<RadioButton>(R.id.rbBulbYes))?.isChecked!!
        }
        binding.rgElectricity.setOnCheckedChangeListener { _, i ->
            viewModel.model.twentyfour_seven_electricity =
                (activity?.findViewById<RadioButton>(R.id.rbElectricityYes))?.isChecked!!
        }
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        viewModel.addLightAmenities(GenericCallBack {
                            if (it.data != null) {
                                showToastInCenter(getString(R.string.amen_saved))
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
            when (INTENT_TYPE) {
                INTENT_TOWER -> {
                    binding.ivTowerImg.setImageURI(uri)
                    binding.ivTowerImg.visibility = View.VISIBLE
                    viewModel.model.tower_image = getRealPathFromURI(uri)

                    /*viewModel.uploadImage(getRealPathFromURI(uri), GenericCallBack {
                        try {
                            var mainobj: JSONObject = JSONObject(it)
                            xloadImages(
                                binding.ivTowerImg,
                                mainobj.optJSONObject("data").optString("messageToshow"),
                                R.drawable.ic_image_placeholder
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })*/
                }
                INTENT_BULB -> {
                    binding.ivBulbImage.setImageURI(uri)
                    binding.ivBulbImage.visibility = View.VISIBLE
                    viewModel.model.bulb_image = getRealPathFromURI(uri)
                }
            }
        }
    }
}