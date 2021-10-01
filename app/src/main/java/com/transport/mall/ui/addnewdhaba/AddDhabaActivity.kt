package com.transport.mall.ui.addnewdhaba

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.ActivityNewDhabaBinding
import com.transport.mall.model.*
import com.transport.mall.ui.addnewdhaba.step1.AmenitiesFragment
import com.transport.mall.ui.addnewdhaba.step1.BankDetailsFragment
import com.transport.mall.ui.addnewdhaba.step1.OwnerDetailsFragment
import com.transport.mall.ui.addnewdhaba.step2.DhabaDetailsFragment
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AddDhabaActivity : BaseActivity<ActivityNewDhabaBinding, AddDhabaVM>(),
    AddDhabaListener {
    override val binding: ActivityNewDhabaBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_new_dhaba
    override var viewModel: AddDhabaVM
        get() = setUpVM(
            this,
            AddDhabaVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    var mIsUpdate = false

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddDhabaActivity::class.java)
            context.startActivity(intent)
        }

        fun startForUpdate(context: Context, dhabaModelMain: DhabaModelMain) {
            val intent = Intent(context, AddDhabaActivity::class.java)
            intent.putExtra("data", dhabaModelMain)
            context.startActivity(intent)
        }

        fun startViewOnly(context: Context, dhabaModelMain: DhabaModelMain) {
            val intent = Intent(context, AddDhabaActivity::class.java)
            intent.putExtra("data", dhabaModelMain)
            intent.putExtra("viewOnly", true)
            context.startActivity(intent)
        }
    }

    override fun bindData() {
        binding.context = this

        //RECEIVING DATA IN CASE OF UPDATING DHABA
        mIsUpdate = intent.hasExtra("data")
        binding.isUpdate = mIsUpdate
//        binding.viewPager.setPagingEnabled(mIsUpdate)
        binding.viewPager.setPagingEnabled(true)
        if (mIsUpdate) {
            viewModel.mDhabaModelMain = intent.getSerializableExtra("data") as DhabaModelMain
        } else {
            SharedPrefsHelper.getInstance(this).getDraftDhaba()?.let {
//                binding.viewPager.setPagingEnabled(true)
                viewModel.mDhabaModelMain = it
            }
        }

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        if (!isAllGranted(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            )
        ) {
            askForPermissions(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) { result ->
                // Check the result, see the Using Results section
                if (!result.isAllGranted(
                        Permission.CAMERA,
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    finish()
                }
            }
        }

        setupToolbar()
        setupStepsFragments()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(if (isUpdate()) getString(R.string.update_dhaba) else getString(R.string.add_new_dhaba))
    }

    private fun setupStepsFragments() {
        binding.viewPager.offscreenPageLimit = 4
        val adapter = HomeViewPagerAdapter(supportFragmentManager)

        val fragment1 = OwnerDetailsFragment()
        val fragment2 = DhabaDetailsFragment()
        val fragment3 = AmenitiesFragment()
        val fragment4 = BankDetailsFragment()

        adapter.addFrag(fragment1, getString(R.string.owner_details))
        adapter.addFrag(fragment2, getString(R.string.dhaba_details))
        adapter.addFrag(fragment3, getString(R.string.amenities))
        adapter.addFrag(fragment4, getString(R.string.bank_details))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.stepnumber = position + 1
                when (position) {
                    0 -> fragment1.youAreInFocus()
                    1 -> fragment2.youAreInFocus()
                    2 -> fragment3.youAreInFocus()
                    3 -> fragment4.youAreInFocus()
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })

        viewModel.mDhabaModelMain.dhabaModel?.let {
            if (it.isDraft.equals("true")) {
                when (viewModel.mDhabaModelMain.draftedAtScreen) {
                    DhabaModelMain.DraftScreen.OwnerDetailsFragment.toString() -> binding.viewPager.currentItem =
                        0
                    DhabaModelMain.DraftScreen.DhabaDetailsFragment.toString() -> binding.viewPager.currentItem =
                        1
                    DhabaModelMain.DraftScreen.AmenitiesFragment.toString() -> binding.viewPager.currentItem =
                        2
                    DhabaModelMain.DraftScreen.BankDetailsFragment.toString() -> binding.viewPager.currentItem =
                        3
                }
            }
        }
    }

    override fun initListeners() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showNextScreen() {
        if (binding.viewPager.currentItem < 3) {
            binding.viewPager.currentItem += 1
        }
    }

    override fun saveAsDraft() {
//        SharedPrefsHelper.getInstance(this).setDraftDhaba(getDhabaModelMain())
        showToastInCenter(getString(R.string.saved_as_draft))
    }

    override fun getDhabaId(): String {
        viewModel.mDhabaModelMain.dhabaModel?.let {
            return it._id
        }
        return ""
    }

    override fun getDhabaModelMain(): DhabaModelMain {
        return viewModel.mDhabaModelMain
    }

    override fun isUpdate(): Boolean {
        return mIsUpdate
    }

    override fun viewOnly(): Boolean {
        return intent.getBooleanExtra("viewOnly", false)
    }

    override fun onBackPressed() {
        if (!isUpdate()) {
            GlobalUtils.showDhabaDiscardAlert(this, getString(R.string.want_to_discard),
                GenericCallBack {
                    when (it) {
                        1 -> { // discard
                            finish()
                            SharedPrefsHelper.getInstance(this).deleteDraftDhaba()
                        }
                        3 -> { // save as draft
                            saveAsDraft()
                            finish()
                        }
                    }
                })
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AmenitiesActivity.FOOD -> {
                    val model = data?.getSerializableExtra("data") as FoodAmenitiesModel
                    model.let { getDhabaModelMain().foodAmenitiesModel = model }
                }
                AmenitiesActivity.PARKING -> {
                    val model = data?.getSerializableExtra("data") as ParkingAmenitiesModel
                    model.let { getDhabaModelMain().parkingAmenitiesModel = model }
                }
                AmenitiesActivity.SLEEPING -> {
                    val model = data?.getSerializableExtra("data") as SleepingAmenitiesModel
                    model.let { getDhabaModelMain().sleepingAmenitiesModel = model }
                }
                AmenitiesActivity.WASHROOM -> {
                    val model = data?.getSerializableExtra("data") as WashroomAmenitiesModel
                    model.let { getDhabaModelMain().washroomAmenitiesModel = model }
                }
                AmenitiesActivity.SECURITY -> {
                    val model = data?.getSerializableExtra("data") as SecurityAmenitiesModel
                    model.let { getDhabaModelMain().securityAmenitiesModel = model }
                }
                AmenitiesActivity.LIGHT -> {
                    val model = data?.getSerializableExtra("data") as LightAmenitiesModel
                    model.let { getDhabaModelMain().lightAmenitiesModel = model }
                }
                AmenitiesActivity.OTHER -> {
                    val model = data?.getSerializableExtra("data") as OtherAmenitiesModel
                    model.let { getDhabaModelMain().otherAmenitiesModel = model }
                }
            }
        }
    }
}