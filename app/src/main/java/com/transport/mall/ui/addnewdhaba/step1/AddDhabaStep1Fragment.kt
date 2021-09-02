package com.transport.mall.ui.addnewdhaba.step1

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.deepakkumardk.videopickerlib.EasyVideoPicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentAddDhabaStep1Binding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.DhabaDetailsModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.createVideoThumbnail
import com.transport.mall.utils.xloadImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep1Fragment :
    BaseFragment<FragmentAddDhabaStep1Binding, AddDhabaStep1VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step1
    override var viewModel: AddDhabaStep1VM
        get() = setUpVM(this, AddDhabaStep1VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep1Binding
        get() = setUpBinding()
        set(value) {}

    var selectedCity: CityAndStateModel? = null
    var selectedState: CityAndStateModel? = null
    var selectedPhoto: String? = null
    var selectedVideo: String? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.data = DhabaDetailsModel()
        viewModel.progressObserver.observeForever{
            if (it) {
                showProgressDialog("dhinak")
            } else {
                hideProgressDialog()
            }
        }

        binding.btnNext.setOnClickListener {

        }
    }

    @SuppressLint("MissingPermission")
    override fun initListeners() {
        setupVideoPickerViews()
        setupImagePicker()
        viewModel.refreshLocation()
        setupLocationViews()
        setupCitiesAndStateView()
    }

    private fun setupCitiesAndStateView() {
        viewModel.getCitiesList(GenericCallBackTwoParams { cityList, StateList ->
            //SET STATES ADAPTER ON SPINNER
            var statesAdapter = ArrayAdapter<CityAndStateModel>(
                activity as Context,
                android.R.layout.simple_list_item_1, StateList
            )
            binding.spnrState.setAdapter(statesAdapter)
            binding.spnrState.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedState = StateList.get(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            })

            //SET CITIES ADAPTER ON SPINNER
            var citiesAdapter = ArrayAdapter<CityAndStateModel>(
                activity as Context,
                android.R.layout.simple_list_item_1, cityList
            )
            binding.spnrCity.setAdapter(citiesAdapter)
            binding.spnrCity.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedCity = cityList.get(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            })
        })
    }

    private fun setupLocationViews() {
        binding.fetchLocationLayout.setOnClickListener {
            showProgressDialog("Fetching Location...")
            viewModel.getCurrentLocation(GenericCallBack { location ->
                hideProgressDialog()
                if (location != null) {
                    binding.edDhabaAddress.setText(
                        viewModel.getAddressUsingLatLong(
                            location.latitude,
                            location.longitude
                        )
                    )
                    Log.e(
                        "Location :::: ",
                        location.latitude.toString() + " : " + location.longitude.toString()
                    )
                } else {
                    Log.e("Location :::: ", "<---- NULL --- >")
                }
            })
        }
    }


    private fun setupImagePicker() {
        binding.llImagePicker.setOnClickListener {
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

    private fun setupVideoPickerViews() {
        binding.llVideoPicker.setOnClickListener {
            GlobalUtils.showOptionsDialog(activity,
                arrayOf("Gallery", "Camera"),
                "Select Video Source",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    when (i) {
                        0 -> {
                            pickVideoFromGallery(this)
                        }
                        1 -> {
                            captureVideo(this)
                        }
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_VIDEO_GALLERY) {
                val list = EasyVideoPicker.getSelectedVideos(data)

                selectedVideo = list?.get(0)?.videoPath!!
                var thumbPath = createVideoThumbnail(activity as Context, selectedVideo!!)
                var uri = Uri.fromFile(thumbPath)
                binding.ivVideoThumb.setImageURI(uri)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else if (requestCode == INTENT_VIDEO_CAMERA) {
                val videoUri: Uri = data?.data!!
                selectedVideo = getRealPathFromURI(videoUri)
                xloadImages(
                    binding.ivVideoThumb,
                    createVideoThumbnail(activity as Context, selectedVideo!!).absolutePath,
                    R.drawable.ic_image_placeholder
                )
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else {
                val uri: Uri = data?.data!!
                selectedPhoto = getRealPathFromURI(uri)
                binding.ivImageThumb.setImageURI(uri)
            }
        }
    }
}