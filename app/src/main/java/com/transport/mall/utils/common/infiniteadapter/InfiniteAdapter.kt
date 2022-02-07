package com.transport.mall.utils.common.infiniteadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.transport.mall.R

abstract class InfiniteAdapter<B : ViewDataBinding> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var binder: B
    private var mShouldLoadMore = true
    private var mIsLoading = false

    // Used to indicate the infinite scrolling should be bottom up
    private var mIsReversedScrolling = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            getLoadingViewHolder(parent)
        } else {
            onCreateView(parent, viewType)
        }
    }

    private var mLoadMoreListener: OnLoadMoreListener? = null

    /**
     * Subclasses should override this method, to actually bind the view data,
     * but always call `super.onBindViewHolder(holder, position)`
     * to enable the adapter to calculate whether the load more callback should be invoked
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mShouldLoadMore && !mIsLoading) {
            val threshold = visibleThreshold
            val hasReachedThreshold = if (mIsReversedScrolling) position <= threshold else position >= getCount() - threshold
            if (hasReachedThreshold) {
                mIsLoading = true
                if (mLoadMoreListener != null) {
                    mLoadMoreListener!!.onLoadMore()
                }
            }
        }
        try {
            if (holder !is LoadingViewHolder) {
                val myViewHolderG = holder as InfiniteAdapter<B>.MyViewHolderG
                bindData(position, myViewHolderG)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingView(position)) {
            VIEW_TYPE_LOADING
        } else {
            val viewType = getViewType(position)
            if (viewType == VIEW_TYPE_LOADING) {
                throw IndexOutOfBoundsException("0 index is reserved for the loading view")
            } else {
                viewType
            }
        }
    }

    override fun getItemCount(): Int {
        val actualCount = getCount()
        // So as to avoid nasty index calculations, having reversed scrolling does
        // not affect the item count.
        // The consequence of this is, while there is more data to load, the first item on
        // the list will be replaced by the loading view
        return if (actualCount == 0 || !mShouldLoadMore || mIsReversedScrolling) {
            actualCount
        } else {
            actualCount + 1
        }
    }

    protected abstract fun bindData(position: Int, myViewHolderG: MyViewHolderG?)

    /**
     * check weather loading is in progress or not.
     *
     * @param position
     * @return
     */
    private fun isLoadingView(position: Int): Boolean {
        // For reversed scrolling, the loading view is always the top one
        val loadingViewPosition = if (mIsReversedScrolling) 0 else getCount()
        return position == loadingViewPosition && mShouldLoadMore
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     *
     *
     * 0 index is reserved for the loading view. So this function cannot return 0.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    fun getViewType(position: Int): Int {
        return 1
    }

    /**
     * Returns the number of scrollable items that should be left (threshold) in the
     * list before `OnLoadMoreListener` will be called
     *
     *
     * You can override this to return a preffered threshold,
     * or leave it to use the default
     *
     * @return integer threshold
     */
    val visibleThreshold: Int
        get() = 1

    /**
     * Returns the loading view to be shown at the bottom of the list.
     *
     * @return loading view
     */
    fun getLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_layout, parent, false))
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item. This is the same as the onCreateViewHolder method in RecyclerView.Adapter,
     * except that it internally detects and handles the creation on the loading footer
     *
     * @param parent
     * @param viewType
     * @return
     */
    fun onCreateView(parent: ViewGroup, viewType: Int): MyViewHolderG {
        binder = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), getInflateLayout(viewType), parent, false
        )
        return MyViewHolderG(binder)
    }

    fun ismIsLoading(): Boolean {
        return mIsLoading
    }

    /**
     * Set as false when you don't want the recycler view to load more data.
     * This will also remove the loading view
     */
    fun setShouldLoadMore(shouldLoadMore: Boolean) {
        mShouldLoadMore = shouldLoadMore
    }

    /**
     * Set as true if you want the endless scrolling to be as the user scrolls
     * to the top of the list, instead of bottom
     */
    fun setIsReversedScrolling(reversed: Boolean) {
        mIsReversedScrolling = reversed
    }

    /**
     * Registers a callback to be notified when there is a need to load more data
     */
    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        mLoadMoreListener = listener
    }

    /**
     * This informs the adapter that `itemCount` more data has been loaded,
     * starting from `positionStart`
     *
     *
     * This also calls `notifyItemRangeInserted(int, int)`,
     * so the implementing class only needs to call this method
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount     Number of items inserted
     */
    fun moreDataLoaded(positionStart: Int, itemCount: Int) {
        try {
            mIsLoading = false
            notifyItemRemoved(positionStart) // remove the loading view
            notifyItemRangeInserted(positionStart, itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * The count of the number of items in the list. This does not include the loading item
     *
     * @return number of items in list
     */
    abstract fun getCount(): Int

    fun removeLoadingView(positionStart: Int) {
        if (mIsLoading) {
            mIsLoading = false
            notifyItemRemoved(positionStart) // remove the loading view
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    /**
     * @return layout of inflator.
     */
    abstract fun getInflateLayout(type: Int): Int

    /**
     * ViewHolder class for the adapter,We are using binding here ,so a single ViewHolder can be used for all.
     */
    inner class MyViewHolderG(var binding: B) : RecyclerView.ViewHolder(binding!!.root)
    companion object {
        private const val VIEW_TYPE_LOADING = 0
    }
}