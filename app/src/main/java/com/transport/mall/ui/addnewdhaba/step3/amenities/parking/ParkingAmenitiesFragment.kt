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
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentParkingAmenitiesBinding
import com.transport.mall.model.ParkingAmenitiesModel
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
    var mListener: AddDhabaListener? = null

    override fun bindData() {
        mListener = activity as AddDhabaListener
        binding.context = activity
        binding.viewmodel = viewModel
//        binding.isUpdate = mListener?.isUpdate()
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        refreshGalleryImages()
        setupFoodPhotosView()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.parkingAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: ParkingAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

        it.concreteParking.let {
            when (it) {
                "1" -> binding.rbConcreteFensed.isChecked = true
                "2" -> binding.rbConcreteOpen.isChecked = true
            }
        }
        it.flatHardParking.let {
            when (it) {
                "1" -> binding.rbFlatHardFensed.isChecked = true
                "2" -> binding.rbFlatHardOpen.isChecked = true
            }
        }
        it.kachaFlatParking.let {
            when (it) {
                "1" -> binding.rbKachaFlatFensed.isChecked = true
                "2" -> binding.rbKachaFlatOpen.isChecked = true
            }
        }
        it.parkingSpace.let {
            when (it) {
                "1" -> binding.rgOnetotwenty.isChecked = true
                "2" -> binding.rgTwentyToFifty.isChecked = true
                "3" -> binding.rgFiftyToHundred.isChecked = true
                "4" -> binding.rgAbove100.isChecked = true
            }
        }

        it.images.let {
            if (it.isNotEmpty()) {
                imageList.addAll(it)
                refreshGalleryImages()
            }
        }
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
                "", uri, getRealPathFromURI(uri)
            )
        )
        refreshGalleryImages()
    }

    private fun refreshGalleryImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerView.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        val adapter = ImageGalleryAdapter(activity as Context, imageList, GenericCallBack {
            viewModel.model.images = imageList
        })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delParkingImg(it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })

        binding.recyclerView.adapter = adapter

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
            viewModel.model.hasEverything(getmContext(),GenericCallBackTwoParams { allOk, message ->
                if (allOk) {
                    if (viewModel.model._id.isNotEmpty()) {
                        viewModel.updateParkingAmenities(GenericCallBack {
                            handleData(it)
                        })
                    } else {
                        viewModel.addParkingAmenities(GenericCallBack {
                            handleData(it)
                        })
                    }
                } else {
                    showToastInCenter(message)
                }
            })
        }
    }

    private fun handleData(it: ApiResponseModel<ParkingAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.parking_amen_saved))
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

            addImageToGallery(uri)
            viewModel.model.images = imageList
        }
    }
}