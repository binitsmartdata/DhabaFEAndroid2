package com.transport.mall.ui.home.helpline

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentEditProfileBinding
import com.transport.mall.model.UserModel
import com.transport.mall.ui.home.profile.ProfileVM
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, ProfileVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_edit_profile
    override var viewModel: ProfileVM
        get() = setUpVM(this, ProfileVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentEditProfileBinding
        get() = setUpBinding()
        set(value) {}

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
                showProgressDialog(getString(R.string.deleting_profile_photo))
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
                    arrayOf(getString(R.string.update_photo), getString(R.string.remove_photo)),
                    getString(R.string.choose_action),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                openImagePicker()
                            }
                            1 -> {
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
                        showToastInCenter(getString(R.string.profile_updated))

                        //NOTIFY THAT USER MODEL IS UPDATED
                        RxBus.publish(it.data!!)
                    } else {
                        showToastInCenter(it.message)
                    }
                })
            }
        }
    }

    private fun openImagePicker() {
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
            binding.ivProfileImg.setImageURI(uri)
            viewModel.userModel.ownerPic = getRealPathFromURI(uri)
        }
    }
}