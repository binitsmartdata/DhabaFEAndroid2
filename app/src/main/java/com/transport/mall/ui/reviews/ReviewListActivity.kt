package com.transport.mall.ui.reviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.ActivityReviewListBinding
import com.transport.mall.ui.viewdhaba.ViewDhabaVM
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.common.GenericCallBack

class ReviewListActivity : BaseActivity<ActivityReviewListBinding, ViewDhabaVM>() {


    override val binding: ActivityReviewListBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_review_list
    override var viewModel: ViewDhabaVM
        get() = setUpVM(
            this,
            ViewDhabaVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ReviewListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun bindData() {
        val reviewAdapter = ReviewAdapter(applicationContext, GenericCallBack {})
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = reviewAdapter
    }

    override fun initListeners() {

        binding.addReview.setOnClickListener {
            val intent = Intent(this@ReviewListActivity, AddReviewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up)
        super.onCreate(savedInstanceState)
    }

}