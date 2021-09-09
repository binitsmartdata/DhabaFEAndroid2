package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentParkingAmenitiesBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
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
        binding.viewmodel = viewModel
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
            ImageGalleryAdapter(activity as Context, imageList, GenericCallBack {
                viewModel.model.images = imageList
            })
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun initListeners() {
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.rgConcreteParking.setOnCheckedChangeListener { radioGroup, id ->
            viewModel.model.concreteParking =
                activity?.findViewById<RadioButton>(id)?.getTag().toString()
        }
        binding.rgFlatHardParking.setOnCheckedChangeListener { radioGroup, id ->
            viewModel.model.flatHardParking =
                activity?.findViewById<RadioButton>(id)?.getTag().toString()

        }
        binding.rgKachaFlatParking.setOnCheckedChangeListener { radioGroup, id ->
            viewModel.model.kachaFlatParking =
                activity?.findViewById<RadioButton>(id)?.getTag().toString()
        }
        binding.rgSpace.setOnCheckedChangeListener { radioGroup, id ->
            viewModel.model.parkingSpace =
                activity?.findViewById<RadioButton>(id)?.getTag().toString()
        }

        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(GenericCallBackTwoParams { allOk, message ->
                if (allOk) {
                    viewModel.addParkingAmenities(GenericCallBack {
                        if (it.data != null) {
                            showToastInCenter(getString(R.string.parking_amen_saved))
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

            addImageToGallery(uri)
            viewModel.model.images = imageList
        }
    }
}