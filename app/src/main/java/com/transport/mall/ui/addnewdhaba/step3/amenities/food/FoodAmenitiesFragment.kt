package com.transport.mall.ui.addnewdhaba.step3.amenities.food

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentFoodAmenitiesBinding
import com.transport.mall.model.FoodAmenitiesModel
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
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
        binding.viewOnly = mListener?.viewOnly()
//        binding.isUpdate = mListener?.isUpdate()

        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }
        refreshGalleryImages()
        setupLicensePhotoViews()
        setupFoodPhotosView()

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.foodAmenitiesModel?.let {
            setData(it)
        }
        setupOptionsListener()
    }

    private fun setData(it: FoodAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

        it.foodLisence?.toBoolean().let {
            binding.rbFoodLicenseYes.isChecked = it
            binding.rbFoodLicenseNo.isChecked = !it
        }

        it.foodAt100?.let {
            when (it) {
                "1" -> binding.rbFoodUnder100.isChecked = true
                "2" -> binding.rbFood81150.isChecked = true
                "3" -> binding.rbFoodAt151To200.isChecked = true
                "4" -> binding.rbFoodAt201To300.isChecked = true
            }
        }

        it.roCleanWater?.toBoolean().let {
            binding.roWaterYes.isChecked = it
            binding.roWaterNo.isChecked = !it
        }
        it.food?.let {
            when (it) {
                "1" -> binding.rbVeg.isChecked = true
                "2" -> binding.rbNonVeg.isChecked = true
                "3" -> binding.rbBothFood.isChecked = true
            }
        }

        it.foodLisenceFile?.let {
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
    }

    private fun setupOptionsListener() {
        binding.rgFoodLicense.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.foodLisence =
                activity?.findViewById<RadioButton>(R.id.rbFoodLicenseYes)?.isChecked.toString()
        }
        binding.rgFoodAt100.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.model.foodAt100 =
                activity?.findViewById<RadioButton>(i)?.getTag().toString()
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
            if (mListener?.viewOnly()!!) {
                ImagePagerActivity.startForSingle(getmContext(), viewModel.model.foodLisenceFile)
            } else {
                if (viewModel.model.foodLisenceFile.trim().isEmpty()) {
                    launchFoodLicensePicker()
                } else {
                    GlobalUtils.showOptionsDialog(
                        getmContext(),
                        arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                        getString(R.string.choose_action),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    ImagePagerActivity.startForSingle(getmContext(), viewModel.model.foodLisenceFile)
                                }
                                1 -> {
                                    launchFoodLicensePicker()
                                }
                            }
                        })
                }
            }
        }
    }

    private fun launchFoodLicensePicker() {
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
            viewModel.delFoodImg(it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun initListeners() {
        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(getmContext(), GenericCallBackTwoParams { allOk, message ->
                if (allOk) {
                    /*if (viewModel.model._id.isNotEmpty()) {
                        viewModel.updateFoodAmenities(GenericCallBack {
                            handleData(it)
                        })
                    } else {
                        viewModel.addFoodAmenities(GenericCallBack {
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

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
    }

    private fun handleData(it: ApiResponseModel<FoodAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.food_amen_saved))
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