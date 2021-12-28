package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentWashroomAmenitiesBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.model.WashroomAmenitiesModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils

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
    var imageList = ArrayList<PhotosModel>()

    override fun bindData() {
        mListener = activity as AddDhabaListener
        binding.context = activity
        binding.viewOnly = mListener?.viewOnly()
//        binding.isUpdate = mListener?.isUpdate()
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        refreshGalleryImages()
        setupLicensePhotoViews()
        setupFoodPhotosView()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.washroomAmenitiesModel?.let {
            setData(it)
        }
    }

    private fun setData(it: WashroomAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

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
                imageList.addAll(it)
                refreshGalleryImages()
            }
        }
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
                        /*if (viewModel.model._id.isNotEmpty()) {
                            viewModel.updatewashroomAmenities(GenericCallBack {
                                handleData(it)
                            })
                        } else {
                            viewModel.addWashroomAmenities(GenericCallBack {
                                handleData(it)
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

    private fun handleData(it: ApiResponseModel<WashroomAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.amen_saved))
            var intent = Intent()
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

    private fun addImageToGallery(uri: Uri) {
        imageList.add(PhotosModel("", getRealPathFromURI(uri)))
        refreshGalleryImages()
    }

    private fun refreshGalleryImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerView.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        val adapter = ImageGalleryAdapter(activity as Context, mListener?.viewOnly(), imageList, GenericCallBack {
            viewModel.model.images = imageList
        })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delWashroomImg(it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }
}