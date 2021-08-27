package com.transport.mall.ui.addnewdhaba

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.shuhart.stepview.StepView
import com.transport.mall.R
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
class NewDhabaActivity : BaseActivity<ActivityNewDhabaBinding, BaseVM>() {
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
            val intent = Intent(context, NewDhabaActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun bindData() {
        if (!isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
            askForPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) { result ->
                // Check the result, see the Using Results section
                if (!result.isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
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
        binding.stepView.getState()
            .selectedTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            .animationType(StepView.ANIMATION_CIRCLE)
            .selectedCircleColor(ContextCompat.getColor(this, R.color.colorAccent))
            .selectedCircleRadius(resources.getDimensionPixelSize(R.dimen.dimen_14))
            .selectedStepNumberColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            ) // You should specify only stepsNumber or steps array of strings.
            // In case you specify both steps array is chosen.
            .steps(object : ArrayList<String?>() {
                init {
                    add("Dhaba Details")
                    add("Owner Details")
                    add("Amenities")
                }
            }) // You should specify only steps number or steps array of strings.
            // In case you specify both steps array is chosen.
//            .stepsNumber(4)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .stepLineWidth(resources.getDimensionPixelSize(R.dimen.dimen_1))
            .textSize(resources.getDimensionPixelSize(R.dimen.font_14))
            .stepNumberTextSize(resources.getDimensionPixelSize(R.dimen.font_16))
            .doneCircleColor(ContextCompat.getColor(this, R.color.colorAccent))
            .doneStepLineColor(ContextCompat.getColor(this, R.color.colorAccent))
            .doneTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            .doneStepMarkColor(ContextCompat.getColor(this, R.color.white))
            .commit()

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
                binding.stepView.go(position, true)
                if (position == 2) {
                    binding.stepView.done(true)
                }
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
}