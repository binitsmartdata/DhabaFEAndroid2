package com.transport.mall.ui.addnewdhaba.step3.amenities.food

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentFoodAmenitiesBinding
import com.transport.mall.model.FoodAmenitiesModel
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class FoodAmenitiesFragment :
    BaseFragment<FragmentFoodAmenitiesBinding, FoodAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_food_amenities
    override var viewModel: FoodAmenitiesVM
        get() = setUpVM(this, FoodAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentFoodAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    lateinit var SELECTED_IMAGE_INTENT_TYPE: IMAGE_INTENT_TYPE
    var imageList = ArrayList<PhotosModel>()
    var mListener: AddDhabaListener? = null

    enum class IMAGE_INTENT_TYPE {
        FOOD_LICENSE,
        FOOD_GALLERY
    }

    override fun bindData() {
        mListener = activity as AddDhabaListener
        viewModel.dhabaModelMain = mListener?.getDhabaModelMain()!!
        binding.context = activity

        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }
        refreshGalleryImages()
        setupLicensePhotoViews()
        setupFoodPhotosView()
        setupOptionsListener()

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: FoodAmenitiesModel) {
        it.foodLisence.toBoolean().let {
            binding.rbFoodLicenseYes.isChecked = it
            binding.rbFoodLicenseNo.isChecked = !it
        }

        it.foodAt100.toBoolean().let {
            binding.rbFoodAt100Yes.isChecked = it
            binding.rbFoodAt100No.isChecked = !it
        }

        it.roCleanWater.toBoolean().let {
            binding.roWaterYes.isChecked = it
            binding.roWaterNo.isChecked = !it
        }

        when (it.food) {
            "1" -> binding.rbVeg.isChecked = true
            "2" -> binding.rbNonVeg.isChecked = true
            "3" -> binding.rbBothFood.isChecked = true
        }

        it.foodLisenceFile.let {
            if (it.isNotEmpty()) {
                xloadImages(
                    binding.ivLicenseImg,
                    it,
                    R.drawable.ic_image_placeholder
                )
                binding.ivLicenseImg.visibility = View.VISIBLE
            }
        }
        it.images.let {
            if (it.isNotEmpty()) {
                imageList.addAll(it)
                refreshGalleryImages()
            }
        }

        binding.btnSaveDhaba.visibility = View.GONE
    }

    private fun setupOptionsListener() {
        binding.rgFoodLicense.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.foodLisence =
                activity?.findViewById<RadioButton>(R.id.rbFoodLicenseYes)?.isChecked.toString()
        }
        binding.rgFoodAt100.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.foodAt100 =
                activity?.findViewById<RadioButton>(R.id.rbFoodAt100Yes)?.isChecked.toString()
        }
        binding.rgRoWater.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.roCleanWater =
                activity?.findViewById<RadioButton>(R.id.roWaterYes)?.isChecked.toString()
        }
        binding.rgFoodType.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.food = activity?.findViewById<RadioButton>(i)?.getTag().toString()
        }
    }

    private fun setupLicensePhotoViews() {
        binding.llLicensePhoto.setOnClickListener {
            SELECTED_IMAGE_INTENT_TYPE = IMAGE_INTENT_TYPE.FOOD_LICENSE
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
        binding.llFoodGallery.setOnClickListener {
            SELECTED_IMAGE_INTENT_TYPE = IMAGE_INTENT_TYPE.FOOD_GALLERY
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

    private fun addImageToGallery(uri: Uri) {
        imageList.add(PhotosModel("0", uri, getRealPathFromURI(uri)))
        refreshGalleryImages()
    }

    private fun refreshGalleryImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerView.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter =
            ImageGalleryAdapter(activity as Context, imageList, GenericCallBack {
                viewModel.model.images = imageList
            })
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun initListeners() {
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(GenericCallBackTwoParams { allOk, message ->
                if (allOk) {
                    viewModel.addFoodAmenities(GenericCallBack {
                        if (it.data != null) {
                            showToastInCenter(getString(R.string.food_amen_saved))
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

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            if (SELECTED_IMAGE_INTENT_TYPE == IMAGE_INTENT_TYPE.FOOD_LICENSE) {
                // Use Uri object instead of File to avoid storage permissions
                binding.ivLicenseImg.setImageURI(uri)
                binding.ivLicenseImg.visibility = View.VISIBLE
                viewModel.model.foodLisenceFile = getRealPathFromURI(uri)
            } else {
                addImageToGallery(uri)
                viewModel.model.images = imageList
            }
        }
    }


}