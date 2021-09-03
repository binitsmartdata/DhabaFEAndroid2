package com.transport.mall.ui.addnewdhaba

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.ActivityNewDhabaBinding
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep1Fragment
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep2Fragment
import com.transport.mall.ui.addnewdhaba.step1.AddDhabaStep3Fragment
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Vishal Sharma on 2019-12-06.
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
        adapter.addFrag(AddDhabaStep1Fragment(), getString(R.string.pending))
        adapter.addFrag(AddDhabaStep2Fragment(), getString(R.string.pending))
        adapter.addFrag(AddDhabaStep3Fragment(), getString(R.string.pending))

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
                binding.stepnumber = position+1
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
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showNextScreen() {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }
}