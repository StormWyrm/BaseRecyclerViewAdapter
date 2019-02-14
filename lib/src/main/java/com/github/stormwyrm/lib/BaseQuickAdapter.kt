package com.github.stormwyrm.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseQuickAdapter<T>(
    @LayoutRes val layoutId: Int,
    var mData: List<T>?
) : RecyclerView.Adapter<BaseViewHolder>() {
    val HEADER_VIEW = 0x00000111
    val FOOTER_VIEW = 0x00000222
    val DEFAULT_VIEW = 0x00000333

    lateinit var mLayoutInflater: LayoutInflater
        private set

    lateinit var mContext: Context
        private set

    var mHeaderLayout: LinearLayout? = null
    var mFooterLayout: LinearLayout? = null

    var onItemClickListener: ((BaseQuickAdapter<*>, View, Int) -> Unit)? = null
    var onItemLongClickListener: ((BaseQuickAdapter<*>, View, Int) -> Boolean)? = null
    var onItemChildClickListener: ((BaseQuickAdapter<*>, View, Int) -> Unit)? = null
    var onItemChildLongClickListener: ((BaseQuickAdapter<*>, View, Int) -> Boolean)? = null

    constructor(layoutId: Int) : this(layoutId, null)

    constructor(data: List<T>?) : this(0, data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        mLayoutInflater = LayoutInflater.from(mContext)
        var baseViewHolder : BaseViewHolder
        when(viewType){
            HEADER_VIEW ->
                baseViewHolder = BaseViewHolder(mHeaderLayout!!)
            FOOTER_VIEW ->
                baseViewHolder = BaseViewHolder(mFooterLayout!!)
            else ->{
                val itemView = mLayoutInflater.inflate(layoutId, parent, false)
                baseViewHolder = BaseViewHolder(itemView)
                baseViewHolder.adapter = this
                bindViewClickListener(baseViewHolder)
            }
        }
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        when(itemViewType){
            DEFAULT_VIEW ->{
                convert(holder, getItem(position - getHeaderLayoutCount()))
            }
        }
    }

    override fun getItemCount(): Int =
        (mData?.size ?: 0) + getHeaderLayoutCount() + getFooterLayoutCount()

    override fun getItemViewType(position: Int): Int {
        val headerNum = getHeaderLayoutCount()
        return if (position < headerNum) {
            HEADER_VIEW
        } else {
            val adjPosition = position - headerNum
            val adapterCount = mData?.size ?: 0

            //判断是否为adapter中的布局
            if (adjPosition < adapterCount)
                DEFAULT_VIEW
            else {
                FOOTER_VIEW
            }
        }
    }

    fun getItem(@IntRange(from = 0) position: Int): T =
        mData?.get(position) ?: throw RuntimeException("the data is empty")

    fun addHeaderView(headerView: View): Int {
        return addHeaderView(headerView, 0)
    }

    fun addHeaderView(headerView: View, index: Int): Int {
        return addHeaderView(headerView, index, LinearLayout.VERTICAL)
    }

    fun addHeaderView(header: View, index: Int, orientation: Int): Int {
        var index = index
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(header.context)
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout?.orientation = LinearLayout.VERTICAL
                mHeaderLayout?.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                mHeaderLayout?.orientation = LinearLayout.HORIZONTAL
                mHeaderLayout?.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        val childCount = mHeaderLayout?.childCount ?: 0
        if (index < 0 || index > childCount) {
            index = childCount
        }
        mHeaderLayout?.addView(header, index)

        //只有添加第一个布局时，才会插入
        if (mHeaderLayout?.childCount == 1) {
            val position = getHeaderLayoutPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    fun addFooterView(footerView: View): Int {
        return addFooterView(footerView, 0)
    }

    fun addFooterView(footerView: View, index: Int): Int {
        return addFooterView(footerView, index, LinearLayout.VERTICAL)
    }

    fun addFooterView(footerView: View, index: Int, orientation: Int): Int {
        var index = index
        if (mFooterLayout == null) {
            mFooterLayout = LinearLayout(footerView.context)
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout?.orientation = LinearLayout.VERTICAL
                mFooterLayout?.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                mFooterLayout?.orientation = LinearLayout.HORIZONTAL
                mFooterLayout?.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        val childCount = mFooterLayout?.childCount ?: 0
        if (index < 0 || index > childCount) {
            index = childCount
        }
        mFooterLayout?.addView(footerView, index)
        if (mFooterLayout?.childCount == 1) {
            val position = getFooterLayoutPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    fun removeHeaderView(headerView: View) {
        if (getHeaderLayoutCount() == 0)
            return
        mHeaderLayout?.removeView(headerView)
        if (mHeaderLayout?.childCount == 0) {
            val position = getHeaderLayoutPosition()
            if (position != -1)
                notifyItemRemoved(position)
        }
    }

    fun removeFooterView(footerView: View) {
        if (getFooterLayoutCount() == 0)
            return
        mFooterLayout?.removeView(footerView)
        if (mFooterLayout?.childCount == 0) {
            val position = getFooterLayoutPosition()
            if (position != -1)
                notifyItemRemoved(position)
        }
    }

    /**
     * if has headerView will be return 1,else will be return 0
     */
    fun getHeaderLayoutCount(): Int {
        var count = mHeaderLayout?.childCount ?: 0
        return if (count > 0) 1 else 0
    }

    fun getFooterLayoutCount(): Int {
        var count = mFooterLayout?.childCount ?: 0
        return if (count > 0) 1 else 0
    }

    private fun getHeaderLayoutPosition(): Int {
        return 0
    }

    private fun getFooterLayoutPosition(): Int {
        return (mData?.size ?: 0) + getHeaderLayoutCount()
    }

    private fun bindViewClickListener(baseViewHolder: BaseViewHolder) {
        if (onItemClickListener != null)
            baseViewHolder.itemView.setOnClickListener {
                onItemClickListener?.invoke(this, it, baseViewHolder.layoutPosition)
            }

        if (onItemLongClickListener != null)
            baseViewHolder.itemView.setOnLongClickListener {
                return@setOnLongClickListener onItemLongClickListener?.invoke(this, it, baseViewHolder.layoutPosition)
                    ?: false
            }
    }

    abstract fun convert(viewHolder: BaseViewHolder, item: T)

}