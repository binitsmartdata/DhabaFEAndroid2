package com.transport.mall.ui.addnewdhaba

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.ActivityNewDhabaBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.FoodAmenitiesModel
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep1Fragment
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep2Fragment
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep3Fragment
import com.transport.mall.ui.addnewdhaba.step1.Step4BankDetailsFragment
import com.transport.mall.ui.addnewdhaba.step3.amenities.AmenitiesActivity
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AddDhabaActivity : BaseActivity<ActivityNewDhabaBinding, BaseVM>(),
    AddDhabaListener {
    override val binding: ActivityNewDhabaBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_new_dhaba
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    var mDhabaModelMain = DhabaModelMain()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddDhabaActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun bindData() {
        binding.context = this
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
        setupStepsIndicator()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(getString(R.string.add_new_dhaba))
    }

    private fun setupStepsIndicator() {

    }

    private fun setupStepsFragments() {
        val adapter = HomeViewPagerAdapter(supportFragmentManager)

        // add your fragments
        adapter.addFrag(AddDhabaStep1Fragment(), getString(R.string.dhaba_details))
        adapter.addFrag(AddDhabaStep2Fragment(), getString(R.string.owner_details))
        adapter.addFrag(AddDhabaStep3Fragment(), getString(R.string.amenities))
        adapter.addFrag(Step4BankDetailsFragment(), getString(R.string.bank_details))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.stepnumber = position + 1
            }

            override fun onPageSelected(position: Int) {

            }

        })
        binding.viewPager.offscreenPageLimit = 3
    }

    override fun initListeners() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showNextScreen() {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    override fun getDhabaId(): String {
        mDhabaModelMain.dhabaModel?.let {
            return it._id
        }
        return ""
    }

    override fun getDhabaModelMain(): DhabaModelMain {
        return mDhabaModelMain
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem > 0) {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        } else {
            GlobalUtils.showConfirmationDialogYesNo(this, "Do you want to discard Dhaba Details?",
                GenericCallBack {
                    if (it!!) {
                        finish()
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AmenitiesActivity.FOOD -> {
                    var model = data?.getSerializableExtra("data") as FoodAmenitiesModel
                    model.let { getDhabaModelMain().foodAmenitiesModel = model }
                }
                AmenitiesActivity.PARKING -> {
                }
                AmenitiesActivity.SLEEPING -> {
                }
                AmenitiesActivity.WASHROOM -> {
                }
                AmenitiesActivity.SECURITY -> {
                }
                AmenitiesActivity.LIGHT -> {
                }
                AmenitiesActivity.OTHER -> {
                }
            }
        }
    }
}