package com.transport.mall.ui.addnewdhaba.step3.amenities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.Menu
import android.view.MenuItem
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.ActivityAmenitiesBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.addnewdhaba.step3.amenities.food.FoodAmenitiesFragment
import com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping.*
import com.transport.mall.ui.addnewdhaba.step3.foodamenities.ParkingAmenitiesFragment
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AmenitiesActivity : BaseActivity<ActivityAmenitiesBinding, BaseVM>(), AddDhabaListener {
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

    private var amanityType: Int = 0
    private lateinit var dhabaModelMain: DhabaModelMain
    private var isUpdate = false

    companion object {
        private const val AMENITY_TYPE = "amenityType"
        private const val DHABA_MODEL = "dhabaId"

        const val FOOD = 1
        const val PARKING = 2
        const val SLEEPING = 3
        const val WASHROOM = 4
        const val SECURITY = 5
        const val LIGHT = 6
        const val OTHER = 7

        fun start(context: Context, amenityType: Int, dhabaModel: DhabaModelMain, isUpdate: Boolean, viewOnly: Boolean) {
            val starter = Intent(context, AmenitiesActivity::class.java)
            starter.putExtra(AMENITY_TYPE, amenityType)
            starter.putExtra(DHABA_MODEL, dhabaModel)
            starter.putExtra("isUpdate", isUpdate)
            starter.putExtra("viewOnly", viewOnly)
            (context as Activity).startActivityForResult(starter, amenityType)
        }
    }

    override fun bindData() {
        dhabaModelMain = intent.getSerializableExtra(DHABA_MODEL) as DhabaModelMain
        amanityType = intent.getIntExtra(AMENITY_TYPE, 0)
        isUpdate = intent.getBooleanExtra("isUpdate", false)
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
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        if (viewOnly()) {
            // removing add/update from screen title when this is viewOnly()
            binding.tvTitle.text = getTitleByType().replace(getString(R.string.add), "", true).replace(getString(R.string.update), "", true)
        } else {
            binding.tvTitle.text = getTitleByType()
        }
        binding.toolbar.getNavigationIcon()?.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
    }

    private fun getTitleByType(): String {
        if (isUpdate) {
            when (amanityType) {
                FOOD -> {
                    if (dhabaModelMain.foodAmenitiesModel != null)
                        return getString(R.string.update_food_amen)
                    else
                        return getString(R.string.food_amen_title)
                }
                PARKING -> {
                    if (dhabaModelMain.parkingAmenitiesModel != null)
                        return getString(R.string.update_parking_amen)
                    else
                        return getString(R.string.parking_amen_title)
                }
                SLEEPING -> {
                    if (dhabaModelMain.sleepingAmenitiesModel != null)
                        return getString(R.string.update_sleeping_amen)
                    else
                        return getString(R.string.sleeping_amen_title)
                }
                WASHROOM -> {
                    if (dhabaModelMain.sleepingAmenitiesModel != null)
                        return getString(R.string.update_washroom_amen)
                    else
                        return getString(R.string.washroom_amen_title)
                }
                SECURITY -> {
                    if (dhabaModelMain.sleepingAmenitiesModel != null)
                        return getString(R.string.update_security_amen)
                    else
                        return getString(R.string.security_amen_title)
                }
                LIGHT -> {
                    if (dhabaModelMain.sleepingAmenitiesModel != null)
                        return getString(R.string.update_light_amen)
                    else
                        return getString(R.string.add_light_amenities)
                }
                OTHER -> {
                    if (dhabaModelMain.sleepingAmenitiesModel != null)
                        return getString(R.string.update_other_amen)
                    else
                        return getString(R.string.other_amen_title)
                }
            }
            return getString(R.string.update_amen)
        } else {
            when (amanityType) {
                FOOD -> return getString(R.string.food_amen_title)
                PARKING -> return getString(R.string.parking_amen_title)
                SLEEPING -> return getString(R.string.sleeping_amen_title)
                WASHROOM -> return getString(R.string.washroom_amen_title)
                SECURITY -> return getString(R.string.security_amen_title)
                LIGHT -> return getString(R.string.add_light_amenities)
                OTHER -> return getString(R.string.other_amen_title)
            }
            return getString(R.string.amen_title)
        }
    }

    override fun initListeners() {
        if (amanityType == PARKING) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                ParkingAmenitiesFragment(),
                "PARKING",
                true
            )
        } else if (amanityType == FOOD) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                FoodAmenitiesFragment(),
                "FOOD",
                true
            )
        } else if (amanityType == SLEEPING) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                SleepingAmenitiesFragment(),
                "SLEEPING",
                true
            )
        } else if (amanityType == WASHROOM) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                WashroomAmenitiesFragment(),
                "WASHROOM",
                true
            )
        } else if (amanityType == SECURITY) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                SecurityAmenitiesFragment(),
                "SECURITY",
                true
            )
        } else if (amanityType == LIGHT) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                LightAmenitiesFragment(),
                "LIGHT",
                true
            )
        } else if (amanityType == OTHER) {
            openFragmentReplaceNoAnim(
                R.id.authContainer,
                OtherAmenitiesFragment(),
                "OTHER",
                true
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.actionClose -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showNextScreen() {
        // NOT APPLICABLE
    }

    override fun showOwnerScreen() {
        //NA
    }

    override fun showDhabaScreen() {
        //NA
    }

    override fun showAmenitiesScreen() {
        //NA
    }

    override fun showBankDetailsScreen() {
        //NA
    }

    override fun saveAsDraft() {
        // NOT APPLICABLE
    }

    override fun getDhabaId(): String {
        return dhabaModelMain.dhabaModel?._id!!
    }

    override fun getDhabaModelMain(): DhabaModelMain {
        return dhabaModelMain
    }

    override fun isUpdate(): Boolean {
        return isUpdate
    }

    override fun viewOnly(): Boolean {
        return intent.getBooleanExtra("viewOnly", false)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_close, menu)
        return true
    }

}