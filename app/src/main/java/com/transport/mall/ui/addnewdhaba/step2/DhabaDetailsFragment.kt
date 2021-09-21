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
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentDhabaDetailsBinding
import com.transport.mall.model.*
import com.transport.mall.ui.addnewdhaba.GoogleMapsActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.GlobalUtils.getAddressUsingLatLong
import com.transport.mall.utils.common.GlobalUtils.getCurrentLocation
import com.transport.mall.utils.common.GlobalUtils.getThumbnailFromVideo
import com.transport.mall.utils.common.GlobalUtils.refreshLocation
import com.transport.mall.utils.common.VideoUtils.getVideoThumbnail
import com.transport.mall.utils.common.VideoUtils.saveVideoToAppScopeStorage
import com.transport.mall.utils.createVideoThumbnail
import com.transport.mall.utils.xloadImages


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaDetailsFragment :
    BaseFragment<FragmentDhabaDetailsBinding, DhabaDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_details
    override var viewModel: DhabaDetailsVM
        get() = setUpVM(this, DhabaDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    var StateList = ArrayList<StateModel>()
    var highwayList = ArrayList<HighwayModel>()
    var statesAdapter: ArrayAdapter<StateModel>? = null
    var highwayAdapter: ArrayAdapter<HighwayModel>? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener

        //SETTING EXISTING DATA ON SCREEN
        showDataIfHas()

        binding.btnNext.isEnabled = !mListener?.isUpdate()!!
        binding.btnSaveDraft.isEnabled = !mListener?.isUpdate()!!
        binding.isUpdate = mListener?.isUpdate()!!
    }

    private fun showDataIfHas() {
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.dhabaModel = it
            setData(it)
        }
    }

    private fun setData(it: DhabaModel) {
        it.images.let {
            xloadImages(binding.ivImageThumb, it, R.drawable.ic_image_placeholder)
        }
        it.videos.let {
            if (it.isNotEmpty()) {
                val bitmap = getThumbnailFromVideo(it)
                if (bitmap != null) {
                    binding.ivVideoThumb.setImageBitmap(bitmap)
                    binding.frameVideoThumb.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun initListeners() {
        setupVideoPickerViews()
        setupImagePicker()
        refreshLocation(activity as Context)
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
                viewModel.dhabaModel.propertyStatus = if (p2 == 0) "" else menuArray[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        //set existing value on property status spinner
        viewModel.dhabaModel.propertyStatus?.let {
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
            if (mListener?.getDhabaModelMain()?.dhabaModel != null) {
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                saveDetails(true)
            }
        }
    }

    private fun saveDetails(isDraft: Boolean) {
        if (!isDraft && mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
        } else {
            viewModel.dhabaModel
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
        //SET LOCALLY SAVED STATES
        AppDatabase.getInstance(getmContext())?.statesDao()
            ?.getAll()?.observe(this, Observer {
                StateList = it as ArrayList<StateModel>
                if (StateList.isNotEmpty()) {
                    setStatesAdapter(StateList)
                }
            })

        AppDatabase.getInstance(getmContext())?.highwayDao()?.getAll()?.observe(this, Observer {
            highwayList = it as ArrayList<HighwayModel>
            setHighwayAdapter(highwayList)
        })
    }

    private fun setStatesAdapter(stateList: ArrayList<StateModel>) {
        statesAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, stateList
        )
        binding.spnrState.setAdapter(statesAdapter)
        binding.spnrState.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.dhabaModel.state = stateList.get(p2).name_en!!
                //GET LIST OF CITIES UNDER SELECTED STATE

                AppDatabase.getInstance(getmContext())?.cityDao()
                    ?.getAllByState(stateList.get(p2).stateCode!!)
                    ?.observe(viewLifecycleOwner, Observer {
                        it?.let { setCitiesAdapter(it as ArrayList<CityModel>) }
                    })
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        })

        viewModel.dhabaModel.state?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in stateList) {
                    if (i.name_en?.equals(it)!!) {
                        binding.spnrState.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
    }

    private fun setHighwayAdapter(StateList: ArrayList<HighwayModel>) {
        highwayAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, StateList
        )
        binding.spnrHighway.setAdapter(highwayAdapter)
        binding.spnrHighway.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.dhabaModel.highway = StateList.get(p2).highwayNumber!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        viewModel.dhabaModel.highway?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in StateList) {
                    if (i.highwayNumber.equals(it)) {
                        binding.spnrHighway.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
    }

    private fun setCitiesAdapter(cityList: ArrayList<CityModel>) {
        //SET CITIES ADAPTER ON SPINNER
        var citiesAdapter = ArrayAdapter<CityModel>(
            activity as Context,
            android.R.layout.simple_list_item_1, cityList
        )
        binding.spnrCity.setAdapter(citiesAdapter)
        binding.spnrCity.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.dhabaModel.city = cityList.get(p2).name_en!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        viewModel.dhabaModel.city?.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in cityList) {
                    if (i.name_en.equals(it)) {
                        binding.spnrCity.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
    }

    private fun setupLocationViews() {
        binding.tvMapPicker.setOnClickListener {
            GoogleMapsActivity.start(this)
        }
        binding.tvCurrLocation.setOnClickListener {
            getCurrentLocation(activity as Context, GenericCallBack { location ->
                if (location != null) {
                    binding.edDhabaAddress.setText(
                        getAddressUsingLatLong(
                            activity as Context,
                            location.latitude,
                            location.longitude
                        ).fullAddress
                    )
                    binding.edPinCode.setText(
                        getAddressUsingLatLong(
                            activity as Context,
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

                viewModel.dhabaModel.videos = list?.get(0)?.videoPath!!
                var thumbPath =
                    createVideoThumbnail(activity as Context, viewModel.dhabaModel.videos)
                var uri = Uri.fromFile(thumbPath)
                binding.ivVideoThumb.setImageURI(uri)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else if (requestCode == INTENT_VIDEO_CAMERA) {
                val videoUri: Uri = data?.data!!

                val bitmap = getVideoThumbnail(activity as Context, videoUri, 200, 200)
                val mimeType: String = activity?.contentResolver?.getType(videoUri)!!
                //Save file to upload on server
                val file = saveVideoToAppScopeStorage(activity as Context, videoUri, mimeType)

                viewModel.dhabaModel.videos = file?.absolutePath!!
                binding.ivVideoThumb.setImageBitmap(bitmap)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else if (requestCode == GoogleMapsActivity.REQUEST_CODE_MAP) {
                val location = data?.getSerializableExtra("data") as LocationAddressModel?
                location.let {
                    viewModel.dhabaModel.address = it?.fullAddress!!
                    viewModel.dhabaModel.latitude = it.latitude.toString()
                    viewModel.dhabaModel.longitude = it.longitude.toString()
                }
            } else {
                val uri: Uri = data?.data!!
                viewModel.dhabaModel.images = getRealPathFromURI(uri)
                binding.ivImageThumb.setImageURI(uri)
            }
        }
    }

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel?.let { viewModel.dhabaModel.owner_id = it._id }
    }
}