package com.transport.mall.ui.addnewdhaba.step2

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import com.transport.mall.model.DhabaModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.createVideoThumbnail
import com.transport.mall.utils.xloadImages


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaDetailsFragment :
    BaseFragment<FragmentAddDhabaStep1Binding, DhabaDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step1
    override var viewModel: DhabaDetailsVM
        get() = setUpVM(this, DhabaDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep1Binding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    var StateList = ArrayList<CityAndStateModel>()
    var statesAdapter: ArrayAdapter<CityAndStateModel>? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.ownerName.set(it?.ownerName) }

        //SETTING EXISTING DATA ON SCREEN
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            setData(it)
        }
    }

    private fun setData(it: DhabaModel) {
        it.name.let {
            viewModel.name.set(it)
        }
        it.ownerName.let {
            viewModel.ownerName.set(it)
        }
        it.mobile.let {
            viewModel.mobile.set(it)
        }
        it.address.let {
            viewModel.address.set(it)
        }
        it.landmark.let {
            viewModel.landmark.set(it)
        }
        it.area.let {
            viewModel.area.set(it)
        }
        it.highway.let {
            viewModel.highway.set(it)
        }
        it.pincode.let {
            viewModel.pincode.set(it)
        }
        it.propertyStatus.let {
            viewModel.propertyStatus.set(it)
        }
        it.state.let {
            viewModel.state.set(it)
        }
        it.city.let {
            viewModel.city.set(it)
        }
        it.images.let {
            xloadImages(binding.ivImageThumb, it, R.drawable.ic_placeholder_outliner)
        }
        it.videos.let {
            xloadImages(binding.ivVideoThumb, it, R.drawable.ic_placeholder_outliner)
        }
    }

    @SuppressLint("MissingPermission")
    override fun initListeners() {
        setupVideoPickerViews()
        setupImagePicker()
        viewModel.refreshLocation()
        setupLocationViews()
        setupCitiesAndStateView()

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        // SET ITEM SELECTED LISTENER ON PROPERTY STATUS SPINNER
        val menuArray = resources.getStringArray(R.array.property_status)
        binding.spnrPropertyStatus.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.propertyStatus.set(if (p2 == 0) "" else menuArray[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        //set existing value on property status spinner
        viewModel.propertyStatus.get()?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in menuArray) {
                    if (i.equals(it)) {
                        binding.spnrPropertyStatus.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }


        binding.btnNext.setOnClickListener {
            if (mListener?.getDhabaModelMain()?.dhabaModel != null) {
                mListener?.showNextScreen()
            } else {
                saveDetails(false)
            }
        }
        binding.btnSaveDraft.setOnClickListener {
            saveDetails(true)
        }
    }

    private fun saveDetails(isDraft: Boolean) {
        if (!isDraft && mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
        } else {
            viewModel.getDhabaModel()
                .hasEverything(GenericCallBackTwoParams { status, message ->
                    if (status) {
                        viewModel.addDhaba(
                            GenericCallBack { response ->
                                if (response.data != null) {
                                    mListener?.getDhabaModelMain()?.dhabaModel = response.data
                                    if (isDraft) {
                                        mListener?.saveAsDraft()
                                        activity?.finish()
                                    } else {
                                        showToastInCenter(getString(R.string.dhaba_created_successfully))
                                        mListener?.showNextScreen()
                                    }
                                } else {
                                    showToastInCenter(response.message)
                                }
                            }
                        )
                    } else {
                        showToastInCenter(message)
                    }
                })
        }
    }

    private fun setupCitiesAndStateView() {
        viewModel.getStatesList(GenericCallBack { it ->
            StateList = it
            //SET STATES ADAPTER ON SPINNER
            setStatesAdapter(StateList)
        })
    }

    private fun setStatesAdapter(StateList: ArrayList<CityAndStateModel>) {
        statesAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, StateList
        )
        binding.spnrState.setAdapter(statesAdapter)
        binding.spnrState.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.state.set(StateList.get(p2).name?.en)
                //GET LIST OF CITIES UNDER SELECTED STATE
                viewModel.getCitiesByStateId(StateList.get(p2).stateCode, GenericCallBack {
                    setCitiesAdapter(it)
                })
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        })

        viewModel.state.get()?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in StateList) {
                    if (i.name?.en?.equals(it)!!) {
                        binding.spnrState.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
    }

    private fun setCitiesAdapter(cityList: ArrayList<CityAndStateModel>) {
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

        viewModel.city.get()?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in cityList) {
                    if (i.name?.en?.equals(it)!!) {
                        binding.spnrCity.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
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
                        ).fullAddress
                    )
                    binding.edPinCode.setText(
                        viewModel.getAddressUsingLatLong(
                            location.latitude,
                            location.longitude
                        ).postalCode
                    )
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
                    R.drawable.ic_placeholder_outliner
                )
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else {
                val uri: Uri = data?.data!!
                viewModel.images.set(if (uri.isAbsolute) uri.path else getRealPathFromURI(uri))
                binding.ivImageThumb.setImageURI(uri)
            }
        }
    }

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel.let { viewModel.ownerName.set(it?.ownerName) }
    }
}