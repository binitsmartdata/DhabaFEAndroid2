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
import androidx.lifecycle.Observer
import com.deepakkumardk.videopickerlib.EasyVideoPicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentAddDhabaStep1Binding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import com.transport.mall.utils.createVideoThumbnail
import com.transport.mall.utils.xloadImages
import java.io.File


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

    var mListener: AddDhabaListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.spnrPropertyStatus.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val menuArray = resources.getStringArray(R.array.property_status)
                viewModel.propertyStatus.set(if (p2 == 0) "" else menuArray[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        binding.btnNext.setOnClickListener {
            viewModel.getDhabaModel().hasEverything(GenericCallBackTwoParams { status, message ->
                if (status) {
                    viewModel.addDhaba(
                        viewModel.getDhabaModel().name,
                        viewModel.getDhabaModel().address,
                        viewModel.getDhabaModel().landmark,
                        viewModel.getDhabaModel().area,
                        viewModel.getDhabaModel().highway,
                        viewModel.getDhabaModel().state,
                        viewModel.getDhabaModel().city,
                        viewModel.getDhabaModel().pincode,
                        viewModel.getDhabaModel().address,
                        "",
                        viewModel.getDhabaModel().propertyStatus,
                        File(viewModel.getDhabaModel().images),
                        File(viewModel.getDhabaModel().videos),
                        SharedPrefsHelper.getInstance(activity as Context).getUserData().id,
                        SharedPrefsHelper.getInstance(activity as Context).getUserData().id,
                        GenericCallBack {
                            if (it.data != null) {
                                showToastInCenter(it.message)
                                mListener?.showNextScreen()
                            } else {
                                showToastInCenter(it.message)
                            }
                        }
                    )
                } else {
                    showToastInCenter(message)
                }
            })
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
                    viewModel.state.set(StateList.get(p2).name?.en)
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
                    viewModel.city.set(cityList.get(p2).name?.en)
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

                viewModel.videos.set(list?.get(0)?.videoPath!!)
                var thumbPath =
                    createVideoThumbnail(activity as Context, viewModel.getDhabaModel().videos)
                var uri = Uri.fromFile(thumbPath)
                binding.ivVideoThumb.setImageURI(uri)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else if (requestCode == INTENT_VIDEO_CAMERA) {
                val videoUri: Uri = data?.data!!
                viewModel.videos.set(getRealPathFromURI(videoUri))
                xloadImages(
                    binding.ivVideoThumb,
                    createVideoThumbnail(
                        activity as Context,
                        viewModel.getDhabaModel().videos
                    ).absolutePath,
                    R.drawable.ic_image_placeholder
                )
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else {
                val uri: Uri = data?.data!!
                viewModel.images.set(if (uri.isAbsolute) uri.path else getRealPathFromURI(uri))
                binding.ivImageThumb.setImageURI(uri)
            }
        }
    }
}