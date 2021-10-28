package com.transport.mall.ui.addnewdhaba.step2

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.database.DhabaTimingModelParent
import com.transport.mall.databinding.FragmentDhabaDetailsBinding
import com.transport.mall.model.*
import com.transport.mall.ui.addnewdhaba.step3.amenities.ImageGalleryAdapter
import com.transport.mall.ui.customdialogs.DialogHighwaySelection
import com.transport.mall.ui.customdialogs.TimingListAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.GlobalUtils.getAddressUsingLatLong
import com.transport.mall.utils.common.GlobalUtils.getCurrentLocation
import com.transport.mall.utils.common.GlobalUtils.showConfirmationDialogYesNo
import com.transport.mall.utils.common.GlobalUtils.showInfoDialog
import com.transport.mall.utils.common.VideoUtils.getVideoThumbnail
import com.transport.mall.utils.common.VideoUtils.processVideo
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


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

    var dhabaTimingModelParent: DhabaTimingModelParent = DhabaTimingModelParent("", ArrayList())

    var userModel: UserModel = UserModel()
    var imageList = ArrayList<PhotosModel>()

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.context = getmContext()
        userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()
        binding.userModel = userModel
        mListener = activity as AddDhabaListener
        binding.isUpdate = mListener?.isUpdate()!!
        binding.viewOnly = mListener?.viewOnly()!!

        //SETTING EXISTING DATA ON SCREEN
        showDataIfHas()
        setupOpeningTimeView()
    }

    private fun showDataIfHas() {
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.dhabaModel = it

            it.images.let {
                if (it.isNotEmpty()) {
                    imageList.addAll(it)
                    refreshGalleryImages()
                }
            }

            it.videos.let { path ->
                if (path.isNotEmpty()) {
                    binding.loadingVideo = true
                    // LOAD VIDEO THUMBNAIL BITMAP IN BACKGROUND
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = GlobalUtils.getThumbnailFromVideo(path)
                        if (bitmap != null) {
                            // UPDATE UI ELEMENT FOR SHOWING VIDEO THUMBNAIL
                            activity?.runOnUiThread(Runnable {
                                binding.loadingVideo = false
                                binding.ivVideoThumb.setImageBitmap(bitmap)
                                binding.frameVideoThumb.visibility = View.VISIBLE
                            })
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun initListeners() {
        setupVideoPickerViews()
        setupImagePicker()
//        refreshLocation(activity as Context)
        setupLocationViews()
        setupCitiesAndStateView()

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.saving_dhaba_details))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_dhaba_details))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserverTimings.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.saving_timing))
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
                    if (i.equals(it, true)) {
                        binding.spnrPropertyStatus.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }

        binding.btnNext.setOnClickListener {
            viewModel.dhabaModel.isDraft = false.toString()
            saveDetails(false)
        }
        binding.btnSaveDraft.setOnClickListener {
            viewModel.dhabaModel.isDraft = true.toString()
            if (mListener?.getDhabaModelMain()?.dhabaModel != null) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.DhabaDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                saveDetails(true)
            }
        }
    }

    private fun initTimingModel(opening: String, closing: String, day: String): DhabaTimingModel {
        val model = DhabaTimingModel()
        model.opening = opening
        model.closing = closing
        model.day = day
        return model
    }

    private fun setupOpeningTimeView() {
        if (mListener?.getDhabaModelMain()?.dhabaTiming != null && mListener?.getDhabaModelMain()?.dhabaTiming?.isNotEmpty()!!) {
            dhabaTimingModelParent.timingArray = mListener?.getDhabaModelMain()?.dhabaTiming
        } else {
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "monday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "tuesday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "wednesday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "thursday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "friday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "saturday"))
            dhabaTimingModelParent.timingArray?.add(initTimingModel("", "", "sunday"))
        }

        binding.timingRV.layoutManager = LinearLayoutManager(getmContext(), LinearLayoutManager.VERTICAL, false)
        binding.timingRV.adapter = TimingListAdapter(getmContext(), dhabaTimingModelParent.timingArray!!)
        binding.timingRV.setHasFixedSize(true)
    }

    private fun saveDetails(isDraft: Boolean) {
        if (mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
        } else {
            if (isDraft) {
                if (viewModel.dhabaModel.name.trim().isNotEmpty()) {
                    proceed(isDraft)
                } else {
                    showToastInCenter(getString(R.string.enter_dhaba_name))
                }
            } else {
                viewModel.dhabaModel.hasEverything(
                    getmContext(),
                    GenericCallBackTwoParams { status, message ->
                        if (status) {
                            // only owner or manager have option to manage timings
                            if (userModel.isOwner() || userModel.isManager()) {
                                val validationMsg = dhabaTimingModelParent.validationMsg(getmContext())
                                if (validationMsg.trim().isNotEmpty()) {
                                    showToastInCenter(validationMsg)
                                } else {
                                    proceed(isDraft)
                                }
                            } else {
                                proceed(isDraft)
                            }
                        } else {
                            showToastInCenter(message)
                        }
                    })
            }
        }
    }

    private fun proceed(isDraft: Boolean) {
        if (viewModel.dhabaModel._id.isNotEmpty()) {
            viewModel.updateDhaba(isDraft,
                GenericCallBack { response ->
                    handleResponse(response, isDraft)
                }
            )
        } else {
            viewModel.addDhaba(isDraft,
                GenericCallBack { response ->
                    handleResponse(response, isDraft)
                }
            )
        }
    }

    private fun handleResponse(response: ApiResponseModel<DhabaModel>, isDraft: Boolean) {
        if (response.data != null) {
            mListener?.getDhabaModelMain()?.dhabaModel = response.data
            viewModel.dhabaModel = response.data!!
            dhabaTimingModelParent.dhabaId = response.data?._id!!

            if (isDraft) {
                // only owner or manager have option to manage timings
                if (userModel.isOwner() || userModel.isManager()) {
                    // update dhaba timing
                    viewModel.addDhabaTimeing(dhabaTimingModelParent, GenericCallBack {
                        if (!it) {
                            showToastInCenter(getString(R.string.error_saving_timing))
                        }
                        // UPDATING DHABA STATUS TO ISDRAFT
                        updateDhabaStatus(isDraft)
                    })
                } else {
                    // UPDATING DHABA STATUS TO ISDRAFT
                    updateDhabaStatus(isDraft)
                }
            } else {
                // only owner or manager have option to manage timings
                if (userModel.isOwner() || userModel.isManager()) {
                    viewModel.addDhabaTimeing(dhabaTimingModelParent, GenericCallBack {
                        if (!it) {
                            showToastInCenter(getString(R.string.error_saving_timing))
                        }
                        showMessageAndGoNext()
                    })
                } else {
                    showMessageAndGoNext()
                }
            }
        } else {
            showToastInCenter(response.message)
        }
    }

    private fun showMessageAndGoNext() {
        if (mListener?.isUpdate()!!) {
            showToastInCenter(getString(R.string.dhaba_updated_successfully))
        } else {
            showToastInCenter(getString(R.string.dhaba_created_successfully))
            mListener?.showNextScreen()
        }
    }

    private fun updateDhabaStatus(isDraft: Boolean) {
        viewModel.updateDhabaStatus(
            isDraft,
            viewModel.dhabaModel,
            null,
            viewModel.progressObserver,
            GenericCallBack {
                if (it.data != null) {
                    mListener?.getDhabaModelMain()?.dhabaModel = it.data

                    mListener?.getDhabaModelMain()?.draftedAtScreen =
                        DhabaModelMain.DraftScreen.DhabaDetailsFragment.toString()
                    mListener?.saveAsDraft()
                    activity?.finish()
                } else {
                    showToastInCenter(it.message)
                }
            })
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
        binding.tvHighway.setOnClickListener {
            AppDatabase.getInstance(getmContext())?.highwayDao()?.getAll()
                ?.observe(this@DhabaDetailsFragment, Observer {
                    DialogHighwaySelection(
                        getmContext(),
                        true,
                        it as ArrayList<HighwayModel>,
                        GenericCallBack {
                            viewModel.dhabaModel.highway = it.highwayNumber!!
                        }).show()
                })
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
            if (GlobalUtils.isLocationEnabled(getmContext())) {
//                GoogleMapsActivity.start(this)
                GlobalUtils.selectLocationOnMap(
                    this,
                    viewModel.dhabaModel.latitude,
                    viewModel.dhabaModel.longitude
                )
            } else {
                GlobalUtils.showConfirmationDialogYesNo(
                    getmContext(),
                    getString(R.string.location_alert_dialog),
                    GenericCallBack {
                        if (it!!) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } else {
//                            GoogleMapsActivity.start(this)
                            GlobalUtils.selectLocationOnMap(
                                this,
                                viewModel.dhabaModel.latitude,
                                viewModel.dhabaModel.longitude
                            )
                        }
                    })
            }
        }
        binding.tvCurrLocation.setOnClickListener {
            if (GlobalUtils.isLocationEnabled(getmContext())) {
                getAddress()
            } else {
                GlobalUtils.showConfirmationDialogYesNo(
                    getmContext(),
                    getString(R.string.location_alert_dialog),
                    GenericCallBack {
                        if (it!!) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } else {
                            showToastInCenter(getString(R.string.unable_to_fetch_address))
                        }
                    })
            }
        }
    }

    private fun getAddress() {
        getCurrentLocation(activity as Context, GenericCallBack { location ->
            if (location != null) {
                viewModel.dhabaModel.latitude = location.latitude.toString()
                viewModel.dhabaModel.longitude = location.longitude.toString()

                getAddressUsingLatLong(
                    activity as Context,
                    location.latitude,
                    location.longitude,
                    GenericCallBack {
                        setAddressAfterConfirmation(it.fullAddress)
                    })
            }
        })
    }

    private fun setAddressAfterConfirmation(address: String?) {
        if (viewModel.dhabaModel.address.toString().isEmpty()) {
            viewModel.dhabaModel.address = address!!
        } else {
            showConfirmationDialogYesNo(
                getmContext(),
                getString(R.string.address_placement_confirmation),
                address!!,
                GenericCallBack {
                    if (it) viewModel.dhabaModel.address = address!!
                })
        }
    }


    private fun setupImagePicker() {
        binding.llPhotos.setOnClickListener {
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
                arrayOf(getString(R.string.gallery), getString(R.string.camera)),
                getString(R.string.select_video_source),
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
                processAndSetVideo(data?.data!!)

            } else if (requestCode == INTENT_VIDEO_CAMERA) {
                processAndSetVideo(data?.data!!)

            } else if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE) {
                // SIMPLE PLACE PICKER RESULY
                viewModel.dhabaModel.latitude =
                    data?.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, -1.toDouble())
                        .toString()
                viewModel.dhabaModel.longitude =
                    data?.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, -1.toDouble())
                        .toString()
//                viewModel.dhabaModel.address = data?.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS)!!
                setAddressAfterConfirmation(data?.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS)!!)
            } else {
                val uri: Uri = data?.data!!
                addImageToGallery(uri)
                viewModel.dhabaModel.images = imageList
            }
        }
    }

    private fun addImageToGallery(uri: Uri) {
        imageList.add(PhotosModel("", uri, getRealPathFromURI(uri)))
        refreshGalleryImages()
    }

    private fun refreshGalleryImages() {
        var columns = GlobalUtils.calculateNoOfColumns(activity as Context, 100f)

        binding.recyclerViewDhabaPics.layoutManager =
            GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)

        val adapter = ImageGalleryAdapter(activity as Context, imageList, GenericCallBack {
            viewModel.dhabaModel.images = imageList
        })
        adapter.setDeletionListener(GenericCallBack {
            viewModel.delDhabaImg(it, GenericCallBack {
                if (it) {
                    showToastInCenter(getString(R.string.photo_deleted))
                }
            })
        })

        binding.recyclerViewDhabaPics.adapter = adapter
        binding.recyclerViewDhabaPics.setHasFixedSize(true)
    }

    private fun processAndSetVideo(videoUri: Uri) {
        processVideo(videoUri, getmContext(), GenericCallBackTwoParams { outputFile, message ->
            if (outputFile == null) {
                showInfoDialog(getmContext(),
                    if (message != null) message else "Unable to process video.",
                    GenericCallBack {

                    })
            } else {
                val bitmap = getVideoThumbnail(activity as Context, videoUri, 200, 200)
                binding.ivVideoThumb.setImageBitmap(bitmap)
                binding.frameVideoThumb.visibility = View.VISIBLE
                viewModel.dhabaModel.videos = outputFile.path
            }
        })
    }

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel?.let { viewModel.dhabaModel.owner_id = it._id }
    }


}