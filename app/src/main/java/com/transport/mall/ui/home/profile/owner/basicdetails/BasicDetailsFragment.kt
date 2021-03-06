package com.transport.mall.ui.home.profile.owner.basicdetails

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentBasicDetailsBinding
import com.transport.mall.model.UserModel
import com.transport.mall.ui.customdialogs.DialogProfileUpdate
import com.transport.mall.ui.home.profile.owner.OwnerProfileVM
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class BasicDetailsFragment : BaseFragment<FragmentBasicDetailsBinding, OwnerProfileVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_basic_details
    override var viewModel: OwnerProfileVM
        get() = setUpVM(this, OwnerProfileVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentBasicDetailsBinding
        get() = setUpBinding()
        set(value) {}

    private val PICKER_PROFILE_IMAGE = 1
    private val PICKER_ID_FRONT = 2
    private val PICKER_ID_BACK = 3
    private var INTENT_TYPE = 0

    var userData: UserModel? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        setUserData()
    }

    private fun setUserData() {
        userData = SharedPrefsHelper.getInstance(activity as Context).getUserData()
        userData?.let {
            viewModel.userModel = it
            binding.userModel = viewModel.userModel
        }
        userData?.mobilePrefix?.let {
            if (it.isNotEmpty()) {
                try {
                    binding.ccpCountryCode.setCountryForPhoneCode(it.toInt())
                } catch (e: Exception) {
                    binding.ccpCountryCode.setCountryForPhoneCode(91)
                }
            }
        }

        userData?.idproofFront?.let {
//                if (it.isNotEmpty()) {
            xloadImages(binding.ivFrontId, it, R.drawable.ic_id_front)
//                }
        }
        userData?.idproofBack?.let {
//                if (it.isNotEmpty()) {
            xloadImages(binding.ivBackId, it, R.drawable.ic_id_back)
//                }
        }
    }

    override fun initListeners() {
        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.userModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
        }

        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_profile))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.deleting_image))
            } else {
                hideProgressDialog()
            }
        })

        binding.ivProfileImg.setOnClickListener {
            if (viewModel.userModel.ownerPic.trim().isEmpty()) {
                openImagePicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo), getString(R.string.remove_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(getmContext(), viewModel.userModel.ownerPic)
                            }
                            1 -> {
                                openImagePicker()
                            }
                            2 -> {
                                viewModel.removeProfileImage({
                                    viewModel.userModel.ownerPic = it.data!!.ownerPic
                                    SharedPrefsHelper.getInstance(getmContext()).setUserData(it.data!!)
                                    GlobalUtils.showToastInCenter(getmContext(), getString(R.string.photo_removed_successfully))

                                    //NOTIFY THAT USER MODEL IS UPDATED
                                    RxBus.publish(it.data!!)
                                })
                            }
                        }
                    })
            }
        }

        binding.uploadBackSideLayout.setOnClickListener {
            Log.e("BACK img :", viewModel.userModel.idproofBack)
            if (viewModel.userModel.idproofBack.trim().isEmpty()) {
                launchBackImgPicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(getmContext(), viewModel.userModel.idproofBack)
                            }
                            1 -> {
                                launchBackImgPicker()
                            }
                        }
                    })
            }
        }

        binding.ivEditPhoto.setOnClickListener {
            openImagePicker()
        }

        binding.btnUpdateProfile.setOnClickListener {
            viewModel.userModel = binding.userModel as UserModel
            if (viewModel.userModel.ownerName.trim().isEmpty()) {
                showToastInCenter(getString(R.string.please_enter_name))
            } else if (viewModel.userModel.isExecutive() && (viewModel.userModel.email.trim().isEmpty() || !GlobalUtils.isValidEmail(viewModel.userModel.email.trim()))) {
                showToastInCenter(getString(R.string.enter_email_id))
            } else if (viewModel.userModel.mobile.trim().isEmpty()) {
                showToastInCenter(getString(R.string.enter_mobile_number))
            } else if (viewModel.userModel.mobile.trim().length < 10) {
                showToastInCenter(getString(R.string.enter_valid_mobile))
            } else {
                viewModel.userModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCodeAsInt.toString()
                viewModel.updateUserProfile(GenericCallBack {
                    if (it.data != null) {
                        viewModel.userModel = it.data!!

                        SharedPrefsHelper.getInstance(getmContext()).setUserData(it.data!!)
//                        showToastInCenter(getString(R.string.profile_updated))
                        var dialog = DialogProfileUpdate(getmContext())
                        dialog.show()
                        dialog.setOnDismissListener {
                            goToHomeScreen()
                        }

                        //NOTIFY THAT USER MODEL IS UPDATED
                        RxBus.publish(it.data!!)
                    } else {
                        showToastInCenter(it.message)
                    }
                })
            }
        }

        binding.uploadFrontSideLayout.setOnClickListener {
            Log.e("front img :", viewModel.userModel.idproofFront)
            if (viewModel.userModel.idproofFront.trim().isEmpty()) {
                launchFrontImgPicker()
            } else {
                GlobalUtils.showOptionsDialog(
                    getmContext(),
                    arrayOf(getString(R.string.view_photo), getString(R.string.update_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                ImagePagerActivity.startForSingle(getmContext(), viewModel.userModel.idproofFront)
                            }
                            1 -> {
                                launchFrontImgPicker()
                            }
                        }
                    })
            }
        }

        binding.ivDeleteFront.setOnClickListener {
            viewModel.deleteIdProofFront(GenericCallBack {
                if (it.data != null) {
                    binding.userModel!!.idproofFront = ""
                    showToastInCenter(getString(R.string.removed_successfully))
                    xloadImages(binding.ivFrontId, it.data!!.idproofFront, R.drawable.ic_id_front)
                    binding.ivDeleteFront.visibility = View.GONE

                    SharedPrefsHelper.getInstance(getmContext()).setUserData(binding.userModel!!)
                    //---------
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
        binding.ivDeleteBack.setOnClickListener {
            viewModel.deleteIdProofBack(GenericCallBack {
                if (it.data != null) {
                    binding.userModel!!.idproofBack = ""
                    showToastInCenter(getString(R.string.removed_successfully))
                    xloadImages(binding.ivBackId, it.data!!.idproofBack, R.drawable.ic_id_back)
                    binding.ivDeleteBack.visibility = View.GONE

                    SharedPrefsHelper.getInstance(getmContext()).setUserData(binding.userModel!!)
                    //---------
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
    }

    private fun launchBackImgPicker() {
        INTENT_TYPE = PICKER_ID_BACK
        launchImagePicker()
    }

    private fun launchFrontImgPicker() {
        INTENT_TYPE = PICKER_ID_FRONT
        launchImagePicker()
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

    private fun openImagePicker() {
        INTENT_TYPE = PICKER_PROFILE_IMAGE
        ImagePicker.with(this)
            .cropSquare()//Crop image(Optional), Check Customization for more option
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
            val uri: Uri = data?.data!!
            when (INTENT_TYPE) {
                PICKER_PROFILE_IMAGE -> {
                    binding.ivProfileImg.setImageURI(uri)
                    viewModel.userModel.ownerPic = getRealPathFromURI(uri)
                }
                PICKER_ID_FRONT -> {
                    binding.ivFrontId.setImageURI(uri)
                    viewModel.userModel.idproofFront = getRealPathFromURI(uri)
                }
                PICKER_ID_BACK -> {
                    binding.ivBackId.setImageURI(uri)
                    viewModel.userModel.idproofBack = getRealPathFromURI(uri)
                }
            }
        }
    }
}