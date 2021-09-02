package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import com.deepakkumardk.videopickerlib.EasyVideoPicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentAddDhabaStep1Binding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.createVideoThumbnail
import com.transport.mall.utils.xloadImages

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep1Fragment :
    BaseFragment<FragmentAddDhabaStep1Binding, AddDhabaStep2VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step1
    override var viewModel: AddDhabaStep2VM
        get() = setUpVM(this, AddDhabaStep2VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep1Binding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
//        binding.vm = viewModel
//        binding.context = activity
    }

    override fun initListeners() {
        setupVideoPickerViews()
        setupImagePicker()
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
                val list = EasyVideoPicker.getSelectedVideos(data)  //ArrayList<VideoModel>

                var thumbPath = createVideoThumbnail(activity as Context, list?.get(0)?.videoPath!!)
                var uri = Uri.fromFile(thumbPath)
                binding.ivVideoThumb.setImageURI(uri)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else if (requestCode == INTENT_VIDEO_CAMERA) {
                val videoUri: Uri = data?.data!!
//            videoView.setVideoURI(videoUri)
                var videoPath = getRealPathFromURI(videoUri)
                xloadImages(
                    binding.ivVideoThumb,
                    createVideoThumbnail(activity as Context, videoPath!!).absolutePath,
                    R.drawable.ic_image_placeholder
                )
//                binding.ivVideoThumb.setImageURI(videoUri)
                binding.frameVideoThumb.visibility = View.VISIBLE
            } else {
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                binding.ivImageThumb.setImageURI(uri)
            }
        }
    }
}