package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.databinding.FragmentOwnerDetailsBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel
import com.transport.mall.ui.customdialogs.DialogDropdownOptions
import com.transport.mall.ui.customdialogs.DialogOwnerSelection
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import com.transport.mall.utils.xloadImages


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OwnerDetailsFragment :
    BaseFragment<FragmentOwnerDetailsBinding, OwnerDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_owner_details
    override var viewModel: OwnerDetailsVM
        get() = setUpVM(this, OwnerDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOwnerDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    private val PICKER_OWNER_IMAGE = 1
    private val PICKER_ID_FRONT = 2
    private val PICKER_ID_BACK = 3
    private var INTENT_TYPE = 0

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()
        mListener = activity as AddDhabaListener
        binding.isUpdate = mListener?.isUpdate()!!
        binding.viewOnly = mListener?.viewOnly()!!

        //SETTING EXISTING DATA ON SCREEN
        setDataIfHas(false)
    }

    private fun setDataIfHas(isSearched: Boolean) {
        mListener?.getDhabaModelMain()?.ownerModel?.let {
            if (!isSearched) {
                viewModel.ownerModel = it
            }

            it.alternativeMobilePrefix?.let {
                if (it.isNotEmpty()) {
                    try {
                        binding.ccpCountryCodeAlt.setCountryForPhoneCode(it.toInt())
                    } catch (e: Exception) {
                        binding.ccpCountryCodeAlt.setCountryForPhoneCode(91)
                    }
                }
            }
            it.mobilePrefix?.let {
                if (it.isNotEmpty()) {
                    try {
                        binding.ccpCountryCode.setCountryForPhoneCode(it.toInt())
                    } catch (e: Exception) {
                        binding.ccpCountryCode.setCountryForPhoneCode(91)
                    }
                }
            }

            it.ownerPic?.let {
//                if (it.isNotEmpty()) {
                xloadImages(binding.ivOwnerImage, it, R.drawable.ic_profile_pic_placeholder)
//                }
            }

            it.idproofFront?.let {
//                if (it.isNotEmpty()) {
                xloadImages(binding.ivFrontId, it, R.drawable.ic_id_front)
//                }
            }
            it.idproofBack?.let {
//                if (it.isNotEmpty()) {
                xloadImages(binding.ivBackId, it, R.drawable.ic_id_back)
//                }
            }
        } ?: kotlin.run {
            binding.ccpCountryCode.setCountryForPhoneCode(91)
            binding.ccpCountryCodeAlt.setCountryForPhoneCode(91)
            viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
            viewModel.ownerModel.alternativeMobilePrefix =
                binding.ccpCountryCodeAlt.selectedCountryCode

            val loggedInUser = SharedPrefsHelper.getInstance(getmContext()).getUserData()
            if (loggedInUser.isOwner() || loggedInUser.isManager()) {
                showExistingUserDetails(loggedInUser)
            }
        }

        // DISABLE PHONE NUMBER VIEW IF SO THAT EXECUTIVE CANNOT CHANGE IT FOR OWNER'S PROFILE
        if (SharedPrefsHelper.getInstance(getmContext()).getUserData().isExecutive()) {
            if (viewModel.ownerModel.mobile.trim()?.isNotEmpty()!!) {
                if (mListener?.isUpdate()!! || isSearched) {
                    binding.edPhoneNumber.isEnabled = false
                }
            }
        }
    }

    override fun initListeners() {
        binding.llAddExisting.setOnClickListener {
            DialogOwnerSelection(getmContext(), GenericCallBack {
                showExistingUserDetails(it)
            }).show()
        }

        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCodeAlt.isClickable = false
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
        }
        binding.ccpCountryCodeAlt.setOnCountryChangeListener {
            viewModel.ownerModel.alternativeMobilePrefix =
                binding.ccpCountryCodeAlt.selectedCountryCode
        }

        setupLocationViews()
        binding.edPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_owner))
            } else {
                hideProgressDialog()
            }
        })

        binding.uploadPictureLayout.setOnClickListener {
            Log.e("PROFILE img :", viewModel.ownerModel.ownerPic)
            if (viewModel.ownerModel.ownerPic.trim().isEmpty()) {
                openOwnerImgPicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(
                                    getmContext(),
                                    viewModel.ownerModel.ownerPic
                                )
                            }
                            1 -> {
                                openOwnerImgPicker()
                            }
                        }
                    })
            }
        }

        binding.uploadFrontSideLayout.setOnClickListener {
            Log.e("front img :", viewModel.ownerModel.idproofFront)
            if (viewModel.ownerModel.idproofFront.trim().isEmpty()) {
                launchFrontImgPicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(
                                    getmContext(),
                                    viewModel.ownerModel.idproofFront
                                )
                            }
                            1 -> {
                                launchFrontImgPicker()
                            }
                        }
                    })
            }
        }
        binding.uploadBackSideLayout.setOnClickListener {
            Log.e("BACK img :", viewModel.ownerModel.idproofBack)
            if (viewModel.ownerModel.idproofBack.trim().isEmpty()) {
                launchBackImgPicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(
                                    getmContext(),
                                    viewModel.ownerModel.idproofBack
                                )
                            }
                            1 -> {
                                launchBackImgPicker()
                            }
                        }
                    })
            }
        }
        binding.btnNext.setOnClickListener {
            GlobalUtils.disableTemporarily(it)
            /* if (mListener?.getDhabaModelMain()?.ownerModel != null && !mListener?.isUpdate()!!) {
                 mListener?.showNextScreen()
             } else {*/
            saveDetails(false)
//            }
        }
        binding.btnSaveDraft.setOnClickListener {
            GlobalUtils.disableTemporarily(it)
            if (mListener?.getDhabaModelMain()?.ownerModel != null) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.OwnerDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                saveDetails(true)
            }
        }

        // SET ITEM SELECTED LISTENER ON ALTERNATIVE CONTACT PERSON DESIGNATION
        val menuArray2 = resources.getStringArray(R.array.alternative_contact_designation)
        var designationAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, menuArray2
        )
        binding.edSelectDesignation.setOnClickListener {
            DialogDropdownOptions(
                getmContext(),
                getString(R.string.alternative_contact_designation),
                designationAdapter,
                {
                    viewModel.ownerModel.alternateDesignation = menuArray2[it]
                }).show()
        }

        binding.ivDeleteFront.setOnClickListener {
            viewModel.deleteIdProofFront(GenericCallBack {
                if (it.data != null) {
                    mListener?.getDhabaModelMain()?.ownerModel?.idproofFront = ""
                    viewModel.ownerModel.idproofFront = ""
                    showToastInCenter(getString(R.string.removed_successfully))
                    xloadImages(binding.ivFrontId, it.data!!.idproofFront, R.drawable.ic_id_front)
                    binding.ivDeleteFront.visibility = View.GONE

                    //UPDATE OWNER'S DETAILS IN USER'S DATA BECAUSE OWNER IS THE SAME USER WHO HAS LOGGED IN
                    if (binding.userModel!!.isOwner() && binding.userModel!!._id.equals(mListener?.getDhabaModelMain()?.ownerModel?._id)) {
                        binding.userModel!!.idproofFront = ""
                        SharedPrefsHelper.getInstance(getmContext())
                            .setUserData(binding.userModel!!)
                    }
                    //---------
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
        binding.ivDeleteBack.setOnClickListener {
            viewModel.deleteIdProofBack(GenericCallBack {
                if (it.data != null) {
                    mListener?.getDhabaModelMain()?.ownerModel?.idproofBack = ""
                    viewModel.ownerModel.idproofBack = ""
                    showToastInCenter(getString(R.string.removed_successfully))
                    xloadImages(binding.ivBackId, it.data!!.idproofBack, R.drawable.ic_id_back)
                    binding.ivDeleteBack.visibility = View.GONE

                    //UPDATE OWNER'S DETAILS IN USER'S DATA BECAUSE OWNER IS THE SAME USER WHO HAS LOGGED IN
                    if (binding.userModel!!.isOwner() && binding.userModel!!._id.equals(mListener?.getDhabaModelMain()?.ownerModel?._id)) {
                        binding.userModel!!.idproofBack = ""
                        SharedPrefsHelper.getInstance(getmContext())
                            .setUserData(binding.userModel!!)
                    }
                    //---------
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
        setRxBusListener()
    }

    private fun launchBackImgPicker() {
        INTENT_TYPE = PICKER_ID_BACK
        launchImagePicker()
    }

    private fun launchFrontImgPicker() {
        INTENT_TYPE = PICKER_ID_FRONT
        launchImagePicker()
    }

    private fun openOwnerImgPicker() {
        INTENT_TYPE = PICKER_OWNER_IMAGE
        ImagePicker.with(this)
            .cropSquare()//Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun setRxBusListener() {
        //LISTENER TO LISTEN WHEN TO EXECUTE SAVE BUTTON
        RxBus.listen(DhabaModelMain.ActiveScreen::class.java).subscribe {
            if (it == DhabaModelMain.ActiveScreen.OwnerDetailsFragment) {
                try {
                    binding.btnNext.performClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showExistingUserDetails(it: UserModel?) {
        mListener?.getDhabaModelMain()?.ownerModel = it
        viewModel.ownerModel.populateData(it)
        setDataIfHas(true)
    }

    private fun setupLocationViews() {
        binding.tvMapPicker.setOnClickListener {
            if (GlobalUtils.isLocationEnabled(getmContext())) {
                GlobalUtils.selectLocationOnMap(
                    this,
                    viewModel.ownerModel.latitude,
                    viewModel.ownerModel.longitude
                )
            } else {
                GlobalUtils.showConfirmationDialogYesNo(
                    getmContext(),
                    getString(R.string.location_alert_dialog),
                    GenericCallBack {
                        if (it!!) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } else {
                            GlobalUtils.selectLocationOnMap(
                                this,
                                viewModel.ownerModel.latitude,
                                viewModel.ownerModel.longitude
                            )
                        }
                    })
            }
        }
        binding.tvCurrLocation.setOnClickListener {

        }
    }

    private fun saveDetails(isDraft: Boolean) {
//        viewModel.ownerModel.mobilePrefix = binding.ccpCountryCode.textView_selectedCountry.text.toString()
        viewModel.ownerModel.hasEverything(
            getmContext(),
            GenericCallBackTwoParams { hasEverything, message ->
                if (hasEverything) {
                    if (isDraft) {
                        // IT WILL ONLY EXECUTE API IF DRAFT BUTTON IS CLICKED
                        if (viewModel.ownerModel._id.isNotEmpty()) {
                            viewModel.updateOwner(GenericCallBack {
                                handleData(it, isDraft)
                            })
                        } else {
                            viewModel.addDhabaOwner(GenericCallBack {
                                handleData(it, isDraft)
                            })
                        }
                    } else {
                        // WHEN NEXT BUTTON IS CLICKED, JUST PUT DATA IN THE MAIN MODEL AND GO TO NEXT SCREEN
                        mListener?.getDhabaModelMain()?.ownerModel = viewModel.ownerModel
                        mListener?.showNextScreen()
                    }
                } else {
                    showToastInCenter(message)
                }
            })
    }

    private fun handleData(it: ApiResponseModel<UserModel>, isDraft: Boolean) {
        if (it.data != null) {
            mListener?.getDhabaModelMain()?.ownerModel = it.data
            viewModel.ownerModel.populateData(it.data!!)

            //UPDATE OWNER'S DETAILS IN USER'S DATA BECAUSE OWNER IS THE SAME USER WHO HAS LOGGED IN
            if (binding.userModel!!.isOwner() && binding.userModel!!._id.equals(mListener?.getDhabaModelMain()?.ownerModel?._id)) {
                binding.userModel!!.populateData(it.data)
                SharedPrefsHelper.getInstance(getmContext()).setUserData(binding.userModel!!)
                //NOTIFY THAT USER MODEL IS UPDATED
                RxBus.publish(it.data!!)
            }
            //---------

            if (isDraft) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.OwnerDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {
                if (mListener?.isUpdate()!!) {
                    showToastInCenter(getString(R.string.updated_successfully))
                    mListener?.showNextScreen()
//                    activity?.finish()
                } else {
                    showToastInCenter(getString(R.string.owner_saved))
                    mListener?.showNextScreen()
                }
            }
        } else {
            showToastInCenter(it.message)
        }
    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop(16f, 9f)
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE) {
                viewModel.ownerModel.address =
                    data?.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS)!!
                viewModel.ownerModel.latitude =
                    data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, -1.toDouble())
                        .toString()
                viewModel.ownerModel.longitude =
                    data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, -1.toDouble())
                        .toString()
            } else {
                val uri: Uri = data?.data!!
                when (INTENT_TYPE) {
                    PICKER_OWNER_IMAGE -> {
                        binding.ivOwnerImage.setImageURI(uri)
                        viewModel.ownerModel.ownerPic = getRealPathFromURI(uri)
                    }
                    PICKER_ID_FRONT -> {
                        binding.ivFrontId.setImageURI(uri)
                        viewModel.ownerModel.idproofFront = getRealPathFromURI(uri)
                    }
                    PICKER_ID_BACK -> {
                        binding.ivBackId.setImageURI(uri)
                        viewModel.ownerModel.idproofBack = getRealPathFromURI(uri)
                    }
                }
            }
        }
    }

    fun youAreInFocus() {

    }
}