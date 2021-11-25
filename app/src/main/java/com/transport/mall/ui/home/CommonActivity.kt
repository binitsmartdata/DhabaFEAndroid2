package com.transport.mall.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.databinding.ActivityCommonBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.home.helpline.RequestForCallFragment
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class CommonActivity : BaseActivity<ActivityCommonBinding, BaseVM>() {
    override val binding: ActivityCommonBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_common
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    private var intentType: Int = 0
    private lateinit var dhabaModelMain: DhabaModelMain
    private var isUpdate = false

    companion object {
        private const val INTENT_TYPE = "INTENT_TYPE"
        private const val TITLE = "TITLE"
        public const val TYPE_REQUEST_FOR_CALL = 1

        fun start(context: Context, intentType: Int, title: String) {
            val starter = Intent(context, CommonActivity::class.java)
            starter.putExtra(INTENT_TYPE, intentType)
            starter.putExtra(TITLE, title)
            (context as Activity).startActivity(starter)
        }
    }

    override fun bindData() {
        intentType = intent.getIntExtra(INTENT_TYPE, 0)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        supportActionBar?.title = intent.getStringExtra(TITLE)
        binding.title = intent.getStringExtra(TITLE)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_empty, menu)
        return true
    }

    override fun initListeners() {
        when (intentType) {
            TYPE_REQUEST_FOR_CALL -> openFragmentReplace(
                R.id.authContainer,
                RequestForCallFragment(),
                "REQUEST_FOR_CALL",
                true
            )
        }
    }
}