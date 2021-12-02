package com.transport.mall.utils.common.fullimageview

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.databinding.ActivityImagespagerBinding
import com.transport.mall.model.PhotosModel
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ImagePagerActivity : BaseActivity<ActivityImagespagerBinding, BaseVM>() {
    override val binding: ActivityImagespagerBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_imagespager
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    private lateinit var data: ArrayList<PhotosModel>
    private var position: Int = 0

    companion object {
        fun start(context: Context, list: ArrayList<PhotosModel>, position: Int) {
            val starter = Intent(context, ImagePagerActivity::class.java)
            starter.putExtra("data", list)
            starter.putExtra("position", position)
            context.startActivity(starter)
        }

        fun startForSingle(context: Context, path: String) {
            val list = ArrayList<PhotosModel>()
            list.add(PhotosModel("", path))
            val starter = Intent(context, ImagePagerActivity::class.java)
            starter.putExtra("data", list)
            starter.putExtra("position", 0)
            context.startActivity(starter)
        }
    }

    override fun bindData() {
        binding.toolbar.getNavigationIcon()?.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)

        data = intent.getSerializableExtra("data") as ArrayList<PhotosModel>
        position = intent.getIntExtra("position", 0)
        setupToolbar()

        val adapter = ImageAdapter(this, data)
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = position
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.tvTitle.text = getString(R.string.appName)
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