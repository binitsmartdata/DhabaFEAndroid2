package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentParkingAmenitiesBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ParkingAmenitiesFragment :
    BaseFragment<FragmentParkingAmenitiesBinding, ParkingAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_parking_amenities
    override var viewModel: ParkingAmenitiesVM
        get() = setUpVM(this, ParkingAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentParkingAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    var imageList = ArrayList<PhotosModel>()

    override fun bindData() {
        binding.context = activity
        refreshGalleryImages()
        setupFoodPhotosView()
    }

    private fun setupFoodPhotosView() {
        binding.llParkingGallery.setOnClickListener {
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
        imageList.add(
            PhotosModel(
                "0", uri, if (uri.isAbsolute) uri.path!! else getRealPathFromURI(
                    uri
                )!!
            )
        )
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

            addImageToGallery(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}