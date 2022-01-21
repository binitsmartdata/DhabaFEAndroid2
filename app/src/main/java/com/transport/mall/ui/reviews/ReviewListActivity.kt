package com.transport.mall.ui.reviews

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.transport.mall.R
import com.transport.mall.databinding.ActivityReviewListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.ui.viewdhaba.ViewDhabaVM
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils

class ReviewListActivity : BaseActivity<ActivityReviewListBinding, ViewDhabaVM>(),
    SwipeRefreshLayout.OnRefreshListener {


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

    lateinit var reviewAdapter: ReviewAdapter

    companion object {
        @JvmStatic
        fun start(context: Context, dhabaModel: DhabaModel) {
            val starter = Intent(context, ReviewListActivity::class.java)
            starter.putExtra("dhabaId", dhabaModel._id)
            starter.putExtra("dhabaModel", dhabaModel)
            context.startActivity(starter)
        }
    }

    override fun bindData() {
        binding.lifecycleOwner = this
        viewModel.dhabaId = GlobalUtils.getNonNullString(intent.getStringExtra("dhabaId"), "")
        binding.dhabaModel = intent.getSerializableExtra("dhabaModel") as DhabaModel

        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.page = 1
        getReviews()
    }

    private fun setAdapter() {
        reviewAdapter = ReviewAdapter(applicationContext, viewModel.reviewList, GenericCallBack {
            AddReviewActivity.start(this, viewModel.dhabaId, viewModel.reviewList[it])
        }, false)
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = reviewAdapter

        reviewAdapter.setOnLoadMoreListener {
            viewModel.page += 1
            getReviews()
        }
    }

    private fun refreshViewsVisibility() {
        if (viewModel.reviewList.isNotEmpty()) {
            binding.recyclerview.visibility = View.VISIBLE
            binding.llNoData.visibility = View.GONE
        } else {
            binding.recyclerview.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }
    }

    private fun getReviews() {
        viewModel.getDhabaReviewsById {
            it?.let {
                if (it.data != null && it.data?.data!!.isNotEmpty()) {

                    binding.tvReviewCount.text =
                        getString(R.string.reviews) + "(" + it.data?.count!! + ")"

                    if (viewModel.page == 1) {
                        viewModel.reviewList.clear()
                        viewModel.reviewList = it.data?.data!!
                        setAdapter()
                    } else {
                        viewModel.reviewList.addAll(it.data?.data!!)
                        reviewAdapter.notifyDataSetChanged()
                    }

                    if (it.data?.data!!.size < viewModel.count) {
                        reviewAdapter.removeLoadingView(viewModel.reviewList.size)
                        reviewAdapter.setShouldLoadMore(false)
                    } else {
                        reviewAdapter.setShouldLoadMore(true)
                    }
                } else {
                    reviewAdapter.removeLoadingView(viewModel.reviewList.size)
                    reviewAdapter.setShouldLoadMore(false)
                }
                refreshViewsVisibility()
            }
        }
    }

    override fun initListeners() {
        binding.addReview.setOnClickListener {
            AddReviewActivity.start(this, viewModel.dhabaId, null)
        }

        viewModel.progressObserver.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = it && viewModel.page == 1
        })

        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.tvClose.setOnClickListener { finish() }
    }

    override fun onRefresh() {
        viewModel.page = 1
        getReviews()
    }

}