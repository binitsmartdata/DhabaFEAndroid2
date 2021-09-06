package com.transport.mall.ui.addnewdhaba.step3.amenities.food

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentFoodAmenitiesBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils

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

    enum class IMAGE_INTENT_TYPE {
        FOOD_LICENSE,
        FOOD_GALLERY
    }

    override fun bindData() {
        binding.context = activity
        refreshGalleryImages()
        setupLicensePhotoViews()
        setupFoodPhotosView()
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
        imageList.add(PhotosModel(uri))
        refreshGalleryImages()
    }

    private fun refreshGalleryImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerView.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter =
            ImageGalleryAdapter(activity as Context, imageList, GenericCallBack { })
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun initListeners() {
        binding.btnSaveDhaba.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            if (SELECTED_IMAGE_INTENT_TYPE == IMAGE_INTENT_TYPE.FOOD_LICENSE) {
                // Use Uri object instead of File to avoid storage permissions
                binding.ivLicenseImg.setImageURI(uri)
            } else {
                addImageToGallery(uri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}