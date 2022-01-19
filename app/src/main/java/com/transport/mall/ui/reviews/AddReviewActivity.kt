package com.transport.mall.ui.reviews

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.viewdhaba.ImageGalleryAdapter
import com.transport.mall.ui.viewdhaba.ViewDhabaVM
import com.transport.mall.utils.base.BaseActivity
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.ActivityAddReviewBinding
import com.transport.mall.model.ReviewModel
import com.transport.mall.utils.common.GlobalUtils


class AddReviewActivity : BaseActivity<ActivityAddReviewBinding, ViewDhabaVM>() {


    override val binding: ActivityAddReviewBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_add_review
    override var viewModel: ViewDhabaVM
        get() = setUpVM(
            this,
            ViewDhabaVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    val REQUEST_IMAGE = 100

    companion object {
        @JvmStatic
        fun start(context: Context, dhabaId: String, reviewModel: ReviewModel?) {
            val starter = Intent(context, AddReviewActivity::class.java)
            starter.putExtra("dhabaId", dhabaId)
            reviewModel?.let {
                starter.putExtra("reviewModel", reviewModel)
            }
            context.startActivity(starter)
        }
    }

    override fun initListeners() {
        viewModel.dhabaId = GlobalUtils.getNonNullString(intent.getStringExtra("dhabaId"), "")
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                GlobalUtils.showProgressDialog(this)
            } else {
                GlobalUtils.hideProgressDialog()
            }
        })

        binding.llUploadImg.setOnClickListener {
            GlobalUtils.showOptionsDialog(
                this,
                arrayOf("Camera", "Gallery"),
                "Select Source",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    when (i) {
                        0 -> {
                            launchCameraIntent()
                        }
                        1 -> {
                            launchGalleryIntent()
                        }
                    }
                })
        }

        binding.addReview.setOnClickListener {
            viewModel.reviewModel.rating = binding.rbRating.rating
            if (viewModel.reviewModel.hasEverything(this)) {
                viewModel.addReviewReply(
                    binding.reviewModel?._id.toString(),
                    binding.edSearch.text.toString(),
                    {
                        if (it) {
                            finish()
                        }
                    })
            }
        }
    }

    override fun bindData() {
        setStatusBarTransparent(this)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        if (intent.hasExtra("reviewModel")) {
            binding.reviewModel = intent.getSerializableExtra("reviewModel") as ReviewModel
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            viewModel.reviewModel.imagesList.add(PhotosModel("", getRealPathFromURI(uri)))
            binding.recyclerViewImages.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewImages.adapter =
                ImageGalleryAdapter(this, viewModel.reviewModel.imagesList)
        }
    }

    fun getRealPathFromURI(contentUri: Uri?): String {
        if (contentUri?.isAbsolute == true) {
            return contentUri.path!!
        } else {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)!!
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }
    }

    private fun launchCameraIntent() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun launchGalleryIntent() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up)
        super.onCreate(savedInstanceState)
    }
}