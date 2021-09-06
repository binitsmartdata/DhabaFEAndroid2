package com.transport.mall.ui.addnewdhaba.step3.amenities

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.databinding.ActivityAmenitiesBinding
import com.transport.mall.ui.addnewdhaba.step3.amenities.food.FoodAmenitiesFragment
import com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping.*
import com.transport.mall.ui.addnewdhaba.step3.foodamenities.ParkingAmenitiesFragment
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AmenitiesActivity : BaseActivity<ActivityAmenitiesBinding, BaseVM>() {
    override val binding: ActivityAmenitiesBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_amenities
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    private lateinit var amanityType: String

    companion object {
        private const val AMENITY_TYPE = "amenityType"
        private const val DHABA_ID = "dhabaId"

        const val FOOD = "food"
        const val PARKING = "parking"
        const val SLEEPING = "sleeping"
        const val WASHROOM = "washroom"
        const val SECURITY = "security"
        const val LIGHT = "LIGHT"
        const val OTHER = "other"

        fun start(context: Context, amenityType: String, dhabaId: String) {
            val starter = Intent(context, AmenitiesActivity::class.java)
            starter.putExtra(AMENITY_TYPE, amenityType)
            starter.putExtra(DHABA_ID, dhabaId)
            context.startActivity(starter)
        }
    }

    override fun bindData() {
        amanityType = intent.getStringExtra(AMENITY_TYPE)
        if (!isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
            askForPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) { result ->
                // Check the result, see the Using Results section
                if (!result.isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
                    finish()
                }
            }
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = getTitleByType()
    }

    private fun getTitleByType(): String {
        when (amanityType) {
            FOOD -> return getString(R.string.food_amen_title)
            PARKING -> return getString(R.string.parking_amen_title)
            SLEEPING -> return getString(R.string.sleeping_amen_title)
            WASHROOM -> return getString(R.string.washroom_amen_title)
            SECURITY -> return getString(R.string.security_amen_title)
            OTHER -> return getString(R.string.other_amen_title)
        }
        return getString(R.string.amen_title)
    }

    override fun initListeners() {
        if (amanityType == PARKING) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                ParkingAmenitiesFragment(),
                PARKING,
                true
            )
        } else if (amanityType == FOOD) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                FoodAmenitiesFragment(),
                FOOD,
                true
            )
        } else if (amanityType == SLEEPING) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                SleepingAmenitiesFragment(),
                SLEEPING,
                true
            )
        } else if (amanityType == WASHROOM) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                WashroomAmenitiesFragment(),
                WASHROOM,
                true
            )
        } else if (amanityType == SECURITY) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                SecurityAmenitiesFragment(),
                SECURITY,
                true
            )
        } else if (amanityType == LIGHT) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                LightAmenitiesFragment(),
                LIGHT,
                true
            )
        } else if (amanityType == OTHER) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                OtherAmenitiesFragment(),
                OTHER,
                true
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}