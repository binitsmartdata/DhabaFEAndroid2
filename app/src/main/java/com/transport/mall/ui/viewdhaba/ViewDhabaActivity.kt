package com.transport.mall.ui.viewdhaba

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.ActivityViewDhabaBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.PhotosModel
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.customdialogs.TimingListAdapter
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.common.fullimageview.ImagePagerActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ViewDhabaActivity : BaseActivity<ActivityViewDhabaBinding, ViewDhabaVM>() {
    override val binding: ActivityViewDhabaBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_view_dhaba
    override var viewModel: ViewDhabaVM
        get() = setUpVM(
            this,
            ViewDhabaVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    var mIsUpdate = false

    companion object {
        fun start(context: Context, dhabaModelMain: DhabaModelMain) {
            val intent = Intent(context, ViewDhabaActivity::class.java)
            intent.putExtra("data", dhabaModelMain)
            context.startActivity(intent)
        }
    }

    override fun bindData() {
        binding.context = this

        //RECEIVING DATA IN CASE OF UPDATING DHABA
        mIsUpdate = intent.hasExtra("data")
        viewModel.mDhabaModelMain = intent.getSerializableExtra("data") as DhabaModelMain
        binding.dhabaModelMain = viewModel.mDhabaModelMain

        // SETUP GOLD, BRONZE, SILVER DHABA CATEGORY
        setupDhabaCategoryView()

        assembleImagesAndShow()

        showDhabaTiming()
    }

    private fun showDhabaTiming() {
        if (!viewModel.mDhabaModelMain.dhabaModel?.open247!!) {
            viewModel.mDhabaModelMain.dhabaTiming?.let {
                for (timing in it) {
                    val today = SimpleDateFormat("EEE").format(Date())
                    if (timing.day.substring(0, 3).equals(today, true)) {
                        if (timing.isEnabled) {
                            binding.tvOpen247.setText(getString(R.string.open_) + " " + timing.opening + " " + getString(R.string.to) + " " + timing.closing)
                            binding.tvOpen247.visibility = View.VISIBLE
                        } else {
                            binding.tvOpen247.visibility = View.GONE
                        }
                        break
                    }
                }
            }
        } else {
            binding.tvOpen247.setText(getString(R.string.open_24_x_7))
            binding.tvOpen247.visibility = View.VISIBLE
        }

        if (viewModel.mDhabaModelMain.dhabaTiming != null && viewModel.mDhabaModelMain.dhabaTiming!!.isNotEmpty()) {
            binding.timingRV.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.timingRV.adapter =
                TimingListAdapter(this, viewModel.mDhabaModelMain.dhabaTiming!!, true)
            binding.timingRV.setHasFixedSize(true)

            binding.viewTimings = false
            binding.flTimingArrow.setOnClickListener {
                binding.viewTimings = !binding.viewTimings!!
                if (binding.viewTimings!!) {
                    binding.ivTimingArrow.rotation = 0f
                } else {
                    binding.ivTimingArrow.rotation = 180f
                }
            }
        } else {
            binding.flTimingArrow.visibility = View.GONE
        }
    }

    private fun assembleImagesAndShow() {
        var imagesList = ArrayList<PhotosModel>()

        // DHABA HOARDING IMAGE
        viewModel.mDhabaModelMain.dhabaModel?.let {
            if (it.images != null) {
                it.images?.let {
                    imagesList.add(PhotosModel("", it))
                }
            }
        }

        // DHABA PHOTOS
        viewModel.mDhabaModelMain.dhabaModel?.let {
            if (it.imageList != null && it.imageList.isNotEmpty()) {
                it.imageList?.let {
                    imagesList.addAll(it)
                }
            }
        }

        // FOOD AMENITIES PHOTOS
        viewModel.mDhabaModelMain.foodAmenitiesModel?.let {
            if (it.images != null && it.images.isNotEmpty()) {
                imagesList.addAll(it.images)
            }
        }
        // PARKING AMENITIES PHOTOS
        viewModel.mDhabaModelMain.parkingAmenitiesModel?.let {
            if (it.images != null && it.images.isNotEmpty()) {
                imagesList.addAll(it.images)
            }
        }
        // SLEEPING AMENITIES PHOTOS
        viewModel.mDhabaModelMain.sleepingAmenitiesModel?.let {
            it.images?.let {
                if (it.isNotEmpty()) {
                    imagesList.add(PhotosModel("", it))
                }
            }
        }
        // WASHROOM AMENITIES PHOTOS
        viewModel.mDhabaModelMain.washroomAmenitiesModel?.let {
            if (it.images != null && it.images.isNotEmpty()) {
                imagesList.addAll(it.images)
            }
        }
        // SECURITY AMENITIES PHOTOS
        viewModel.mDhabaModelMain.securityAmenitiesModel?.let {
            if (it.indoorCameraImage != null && it.indoorCameraImage.isNotEmpty()) {
                imagesList.addAll(it.indoorCameraImage)
            }
            if (it.outdoorCameraImage != null && it.outdoorCameraImage.isNotEmpty()) {
                imagesList.addAll(it.outdoorCameraImage)
            }
        }
        // LIGHT AMENITIES PHOTOS
        viewModel.mDhabaModelMain.lightAmenitiesModel?.let {
            it.towerLightImage?.let {
                if (it.isNotEmpty()) {
                    imagesList.add(PhotosModel("", it))
                }
            }
            it.bulbLightImage?.let {
                if (it.isNotEmpty()) {
                    imagesList.add(PhotosModel("", it))
                }
            }
        }
        // OTHER AMENITIES PHOTOS
        viewModel.mDhabaModelMain.otherAmenitiesModel?.let {
            if (it.barberImages != null && it.barberImages.isNotEmpty()) {
                imagesList.addAll(it.barberImages)
            }
        }

        binding.recyclerViewImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewImages.adapter = ImageGalleryAdapter(this, imagesList)
    }

    private fun setupDhabaCategoryView() {
        if (viewModel.mDhabaModelMain.dhabaModel?.dhabaCategory.equals(viewModel.mDhabaModelMain.dhabaModel?.CATEGORY_GOLD, true)) {
            binding.tvCategory.setBackgroundResource(R.drawable.ic_gold_hotel_type)
        } else if (viewModel.mDhabaModelMain.dhabaModel?.dhabaCategory.equals(viewModel.mDhabaModelMain.dhabaModel?.CATEGORY_BRONZE, true)) {
            binding.tvCategory.setBackgroundResource(R.drawable.ic_bronze_hotel_type)
        } else {
            binding.tvCategory.setBackgroundResource(R.drawable.ic_silver_hotel_type)
        }
    }

    override fun initListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivHoardingPic.setOnClickListener {
            ImagePagerActivity.startForSingle(this, viewModel.mDhabaModelMain.dhabaModel?.images!!)
        }
        binding.llFoodAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.FOOD, viewModel.mDhabaModelMain, true, true)
        }
        binding.llParkingAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.PARKING, viewModel.mDhabaModelMain, true, true)
        }
        binding.llSleepingAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.SLEEPING, viewModel.mDhabaModelMain, true, true)
        }
        binding.llWashroomAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.WASHROOM, viewModel.mDhabaModelMain, true, true)
        }
        binding.llSecurityAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.SECURITY, viewModel.mDhabaModelMain, true, true)
        }
        binding.llLightAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.LIGHT, viewModel.mDhabaModelMain, true, true)
        }
        binding.llOtherAmenities.setOnClickListener {
            AmenitiesActivity.start(this, AmenitiesActivity.OTHER, viewModel.mDhabaModelMain, true, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}