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
        val itemView = mLayoutInflater.inflate(layoutId, parent, false)
        val baseViewHolder = BaseViewHolder(itemView)
        baseViewHolder.adapter = this
        bindViewClickListener(baseViewHolder)
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, getItem(position))
    }

    override fun getItemCount(): Int = mData?.size ?: 0

    fun addHeaderView(headerView: View): Int {
        return addHeaderView(headerView, -1)
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
        if (mHeaderLayout?.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }


    /**
     * if has headerView will be return 1,else will be return 0
     */
    fun getHeaderLayoutCount(): Int {
        var count = mHeaderLayout?.childCount ?: 0
        if (count > 0)
            return 1
        else
            return 0
    }

    private fun getHeaderViewPosition(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getItem(@IntRange(from = 0) position: Int): T =
        mData?.get(position) ?: throw RuntimeException("the data is empty")

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