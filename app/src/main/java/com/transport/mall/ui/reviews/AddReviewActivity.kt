package com.transport.mall.ui.reviews

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.transport.mall.R
import com.transport.mall.databinding.ActivityAddReviewBinding
import com.transport.mall.ui.viewdhaba.ViewDhabaVM
import com.transport.mall.utils.base.BaseActivity

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



    override fun initListeners() {
    }

    override fun bindData() {
    }
}