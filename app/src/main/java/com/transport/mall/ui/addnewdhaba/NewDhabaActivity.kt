package com.transport.mall.ui.addnewdhaba

import android.content.Context
import androidx.core.content.ContextCompat
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.baoyachi.stepview.bean.StepBean
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

    override fun bindData() {
        if (!isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
            askForPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) { result ->
                // Check the result, see the Using Results section
                if (!result.isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
                    finish()
                }
            }
        }

        setupStepsFragments()
    }

    private fun setupStepsFragments() {
        val adapter = HomeViewPagerAdapter(supportFragmentManager)

        // add your fragments
        adapter.addFrag(AddDhabaStep1Fragment(), getString(R.string.pending))
        adapter.addFrag(AddDhabaStep2Fragment(), getString(R.string.pending))
        adapter.addFrag(AddDhabaStep3Fragment(), getString(R.string.pending))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter

        val stepsBeanList: MutableList<StepBean> = ArrayList()
        stepsBeanList.add(StepBean("1", 1));
        stepsBeanList.add(StepBean("2", 2));
        stepsBeanList.add(StepBean("3", 3));
        binding.stepView
            .setStepViewTexts(stepsBeanList)//总步骤
            .setTextSize(12)//set textSize
            .setStepsViewIndicatorCompletedLineColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )//设置StepsViewIndicator完成线的颜色
            .setStepsViewIndicatorUnCompletedLineColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )//设置StepsViewIndicator未完成线的颜色
            .setStepViewComplectedTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )//设置StepsView text完成线的颜色
            .setStepViewUnComplectedTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )//设置StepsView text未完成线的颜色
            .setStepsViewIndicatorCompleteIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_tick_step
                )
            )//设置StepsViewIndicator CompleteIcon
            .setStepsViewIndicatorDefaultIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_circle_black
                )
            )//设置StepsViewIndicator DefaultIcon
            .setStepsViewIndicatorAttentionIcon(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.attention
                )
            );//设置StepsViewIndicator AttentionIcon

    }

    override fun initListeners() {

    }
}