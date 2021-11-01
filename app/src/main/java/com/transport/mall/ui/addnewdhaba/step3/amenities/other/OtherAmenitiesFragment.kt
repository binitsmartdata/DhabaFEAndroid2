package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

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
import com.transport.mall.databinding.FragmentOtherAmenitiesBinding
import com.transport.mall.model.OtherAmenitiesModel
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OtherAmenitiesFragment :
    BaseFragment<FragmentOtherAmenitiesBinding, OtherAmenitiesVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_other_amenities
    override var viewModel: OtherAmenitiesVM
        get() = setUpVM(this, OtherAmenitiesVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOtherAmenitiesBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null
    var imageList = ArrayList<PhotosModel>()

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as AddDhabaListener
        binding.context = activity
        binding.viewOnly = mListener?.viewOnly()
//        binding.isUpdate = mListener?.isUpdate()
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.model.dhaba_id = it._id
        }

        refreshGalleryImages()
        setupLicensePhotoViews()
        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.otherAmenitiesModel?.let {
            showData(it)
        }
    }

    private fun showData(it: OtherAmenitiesModel) {
        viewModel.model = it
        binding.isUpdate = viewModel.model._id.isNotEmpty()

        it.mechanicShop.let {
            when (it) {
                0 -> binding.rbMechNo.isChecked = true
                1 -> {
                    binding.rbMerch2771.isChecked = true
                    binding.rbMechYes.isChecked = true
                }
                2 -> {
                    binding.rbMerch2772.isChecked = true
                    binding.rbMechYes.isChecked = true
                }
                3 -> {
                    binding.rbMerch2773.isChecked = true
                    binding.rbMechYes.isChecked = true
                }
            }
        }
        it.mechanicShopType.let {
            when (it) {
                0 -> binding.rbMechNo.isChecked = true
                1 -> {
                    binding.rbMech24.isChecked = true
                    binding.rbMechYes.isChecked = true
                }
                2 -> {
                    binding.rbMechDay.isChecked = true
                    binding.rbMechYes.isChecked = true
                }
            }
        }
        it.punctureshop.let {
            when (it) {
                0 -> binding.rbPuncture247No.isChecked = true
                1 -> {
                    binding.rbPuncture2471.isChecked = true
                    binding.rbPuncture247Yes.isChecked = true
                }
                2 -> {
                    binding.rbPuncture2472.isChecked = true
                    binding.rbPuncture247Yes.isChecked = true
                }
                3 -> {
                    binding.rbPuncture2473.isChecked = true
                    binding.rbPuncture247Yes.isChecked = true
                }
            }
        }
        it.punctureShopType.let {
            when (it) {
                0 -> binding.rbPuncture247No.isChecked = true
                1 -> {
                    binding.rbPuncture24.isChecked = true
                    binding.rbPuncture247Yes.isChecked = true
                }
                2 -> {
                    binding.rbPunctureDay.isChecked = true
                    binding.rbPuncture247Yes.isChecked = true
                }
            }
        }

        it.dailyutilityshop.let {
            when (it) {
                0 -> binding.rbUtilityNo.isChecked = true
                1 -> {
                    binding.rbUtility2471.isChecked = true
                    binding.rbUtilityYes.isChecked = true
                }
                2 -> {
                    binding.rbUtility2472.isChecked = true
                    binding.rbUtilityYes.isChecked = true
                }
                3 -> {
                    binding.rbUtility2473.isChecked = true
                    binding.rbUtilityYes.isChecked = true
                }
            }
        }
        it.utilityShopType.let {
            when (it) {
                0 -> binding.rbUtilityNo.isChecked = true
                1 -> {
                    binding.rbUtility24.isChecked = true
                    binding.rbUtilityYes.isChecked = true
                }
                2 -> {
                    binding.rbUtilityDay.isChecked = true
                    binding.rbUtilityYes.isChecked = true
                }
            }
        }

        it.barber.let {
            when (it) {
                0 -> binding.rbBarberNo.isChecked = true
                1 -> {
                    binding.rbBarber1.isChecked = true
                    binding.rbBarberYes.isChecked = true
                }
                2 -> {
                    binding.rbBarber2.isChecked = true
                    binding.rbBarberYes.isChecked = true
                }
                3 -> {
                    binding.rbBarber3.isChecked = true
                    binding.rbBarberYes.isChecked = true
                }
            }
        }
        it.barberShopType.let {
            when (it) {
                0 -> binding.rbBarberNo.isChecked = true
                1 -> {
                    binding.rbBarber24.isChecked = true
                    binding.rbBarberYes.isChecked = true
                }
                2 -> {
                    binding.rbBarberDay.isChecked = true
                    binding.rbBarberYes.isChecked = true
                }
            }
        }

        it.barberImages.let {
            if (it.isNotEmpty()) {
                imageList.addAll(it)
                refreshGalleryImages()
            }
        }
    }

    private fun setupLicensePhotoViews() {
        binding.flBarberImg.setOnClickListener {
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

        // YES NO BUTTONS IMPLEMENTATION
        binding.rgMechYesNo.setOnCheckedChangeListener { radioGroup, i ->
            val enabled = (activity?.findViewById<RadioButton>(i))?.getTag()?.toString().toBoolean()
            viewModel.model.mechanicShopEnabled = enabled
            if (!enabled) {
                binding.rgMerch277.clearCheck()
                binding.rgMechDay24.clearCheck()
            }
        }
        binding.rgPuncture247YesNo.setOnCheckedChangeListener { radioGroup, i ->
            val enabled = (activity?.findViewById<RadioButton>(i))?.getTag()?.toString().toBoolean()
            viewModel.model.punctureshopEnabled = enabled
            if (!enabled) {
                binding.rgPuncture247.clearCheck()
                binding.rgPunctureDay24.clearCheck()
            }
        }
        binding.rgUtilityYesNo.setOnCheckedChangeListener { radioGroup, i ->
            val enabled = (activity?.findViewById<RadioButton>(i))?.getTag()?.toString().toBoolean()
            viewModel.model.dailyutilityEnabled = enabled
            if (!enabled) {
                binding.rgUtility247.clearCheck()
                binding.rgUtilityDay24.clearCheck()
            }
        }
        binding.rgBarberYesNo.setOnCheckedChangeListener { radioGroup, i ->
            val enabled = (activity?.findViewById<RadioButton>(i))?.getTag()?.toString().toBoolean()
            viewModel.model.barberEnabled = enabled
            if (!enabled) {
                binding.rgBarberDay24.clearCheck()
                binding.rgBarber.clearCheck()
            }
        }
        // YES NO BUTTONS IMPLEMENTATION -------------

        binding.rgMerch277.setOnCheckedChangeListener { _, i ->
            viewModel.model.mechanicShop = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }
        binding.rgMechDay24.setOnCheckedChangeListener { _, i ->
            viewModel.model.mechanicShopType = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }

        binding.rgPuncture247.setOnCheckedChangeListener { _, i ->
            viewModel.model.punctureshop = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }
        binding.rgPunctureDay24.setOnCheckedChangeListener { _, i ->
            viewModel.model.punctureShopType = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }

        binding.rgUtility247.setOnCheckedChangeListener { _, i ->
            viewModel.model.dailyutilityshop = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }
        binding.rgUtilityDay24.setOnCheckedChangeListener { _, i ->
            viewModel.model.utilityShopType = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }

        binding.rgBarber.setOnCheckedChangeListener { _, i ->
            viewModel.model.barber = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }
        binding.rgBarberDay24.setOnCheckedChangeListener { _, i ->
            viewModel.model.barberShopType = GlobalUtils.getNonNullString(
                (activity?.findViewById<RadioButton>(i))?.tag.toString(),
                "0"
            ).toInt()
        }

        binding.btnSkip.setOnClickListener { activity?.finish() }

        binding.btnSaveDhaba.setOnClickListener {
            viewModel.model.hasEverything(
                getmContext(),
                GenericCallBackTwoParams { allOk, message ->
                    if (allOk) {
                        if (viewModel.model._id.isNotEmpty()) {
                            viewModel.updateOtherAmenities(GenericCallBack {
                                handleData(it)
                            })
                        } else {
                            viewModel.addOtherAmenities(GenericCallBack {
                                handleData(it)
                            })
                        }
                    } else {
                        showToastInCenter(message)
                    }
                })

        }
    }

    private fun handleData(it: ApiResponseModel<OtherAmenitiesModel>) {
        if (it.data != null) {
            showToastInCenter(getString(R.string.amen_saved))
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
            viewModel.model.barberImages = imageList
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
            viewModel.model.barberImages = imageList
        })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delBarberImg(it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }
}