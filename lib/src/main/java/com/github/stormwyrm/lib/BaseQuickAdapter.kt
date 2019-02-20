package com.github.stormwyrm.lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.stormwyrm.lib.anim.*

abstract class BaseQuickAdapter<T>(
    @LayoutRes val layoutId: Int,
    var mData: List<T>?
) : RecyclerView.Adapter<BaseViewHolder>() {
    companion object {
        const val ALPHAIN = 0x00000001
        const val SCALEIN = 0x00000002
        const val SLIDEIN_BOTTOM = 0x00000003
        const val SLIDEIN_LEFT = 0x00000004
        const val SLIDEIN_RIGHT = 0x00000005
    }

    private val HEADER_VIEW = 0x00000111
    private val FOOTER_VIEW = 0x00000222
    private val EMPTY_VIEW = 0x00000333
    private val DEFAULT_VIEW = 0x00000444

    lateinit var mLayoutInflater: LayoutInflater
        private set

    private var mHeaderLayout: LinearLayout? = null
    private var mFooterLayout: LinearLayout? = null
    private var mEmptyLayout: FrameLayout? = null

    var onItemClickListener: ((BaseQuickAdapter<*>, View, Int) -> Unit)? = null
    var onItemLongClickListener: ((BaseQuickAdapter<*>, View, Int) -> Boolean)? = null
    var onItemChildClickListener: ((BaseQuickAdapter<*>, View, Int) -> Unit)? = null
    var onItemChildLongClickListener: ((BaseQuickAdapter<*>, View, Int) -> Boolean)? = null

    var isOpenAnimation: Boolean = false //是否开启动画
        private set
    var isFirstOpenOnly: Boolean = true //仅仅第一次进入时候加载动画
    var animDuration: Long = 300
    var mLastPositioin: Int = -1 //上一次加载动画的item，用于判断是否加载过动画
        private set
    var defaultAnimation: BaseAnimation = AlphaInAnimation()

    constructor(layoutId: Int) : this(layoutId, null)

    constructor(data: List<T>?) : this(0, data)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //解决在GridLayoutManager模式下header和Footer无法占满一行
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    //根据位置类型 设置item可以占据几行
                    val itemViewType = getItemViewType(position)
                    if (isFixedViewType(itemViewType)) {
                        return layoutManager.spanCount
                    }
                    return 1
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mLayoutInflater = LayoutInflater.from(parent.context)
        var baseViewHolder: BaseViewHolder
        when (viewType) {
            HEADER_VIEW ->
                baseViewHolder = BaseViewHolder(mHeaderLayout!!)
            FOOTER_VIEW ->
                baseViewHolder = BaseViewHolder(mFooterLayout!!)
            EMPTY_VIEW -> {
                baseViewHolder = BaseViewHolder(mEmptyLayout!!)
            }
            else -> {
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
        when (itemViewType) {
            DEFAULT_VIEW -> {
                convert(holder, getItem(position - getHeaderLayoutCount()))
            }
        }
    }

    override fun getItemCount(): Int =
        if (getEmptyLayoutCount() == 1) {
            getHeaderLayoutCount() + getEmptyLayoutCount() + getFooterLayoutCount()
        } else {
            (mData?.size ?: 0) + getHeaderLayoutCount() + getFooterLayoutCount()
        }

    override fun getItemViewType(position: Int): Int {
        //有Empty时候，itemcount最大为3 最小为1
        if (getEmptyLayoutCount() == 1) {
            val hasHeader = getHeaderLayoutCount() == 1
            when (position) {
                0 -> return if (hasHeader) {
                    HEADER_VIEW
                } else
                    EMPTY_VIEW

                1 -> return if (hasHeader)
                    FOOTER_VIEW
                else
                    EMPTY_VIEW

                2 -> return FOOTER_VIEW
            }
        }

        val headerNum = getHeaderLayoutCount()
        return if (position < headerNum) {
            HEADER_VIEW
        } else {
            val adjPosition = position - headerNum
            //在存在EmptyView时候
            val adapterCount = mData?.size ?: 0
            //判断是否为adapter中的布局
            if (adjPosition < adapterCount)
                DEFAULT_VIEW
            else {
                FOOTER_VIEW
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }


    fun setNewData(newData: List<T>) {
        mData = newData
        notifyDataSetChanged()
    }

    fun getItem(@IntRange(from = 0) position: Int): T =
        mData?.get(position) ?: throw RuntimeException("the data is empty")


    fun addHeaderView(headerView: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        var index = index
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(headerView.context)
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
        mHeaderLayout?.addView(headerView, index)

        //只有添加第一个布局时，才会插入
        if (mHeaderLayout?.childCount == 1) {
            val position = getHeaderLayoutPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }


    fun addFooterView(footerView: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
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

    fun setEmptyView(emptyView: View) {
        var insert = false//插入RecyclerView中
        if (mEmptyLayout == null) {
            mEmptyLayout = FrameLayout(emptyView.context)
            val layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            val lp = emptyView.layoutParams
            if (lp != null) {
                layoutParams.width = lp.width
                layoutParams.height = lp.height
            }
            mEmptyLayout?.layoutParams = layoutParams
            insert = true
        }
        mEmptyLayout?.run {
            removeAllViews()
            addView(emptyView)
        }
        if (insert && getEmptyLayoutCount() == 1) {
            val position = getEmptyLayoutPosition()
            if (position != -1)
                notifyItemInserted(position)
        }
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

    fun removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0)
            return
        mHeaderLayout?.removeAllViews()
        //清除所有的子View
        if (getHeaderLayoutCount() == 0) {
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

    fun removeAllFooterView() {
        if (getFooterLayoutCount() == 0)
            return
        mHeaderLayout?.removeAllViews()
        //清除所有的子View
        if (getFooterLayoutCount() == 0) {
            val position = getFooterLayoutPosition()
            if (position != -1)
                notifyItemRemoved(position)
        }
    }

    fun openLoadAnimation(animationType: Int = -1) {
        isOpenAnimation = true
        when (animationType) {
            ALPHAIN ->
                defaultAnimation = AlphaInAnimation()
            SCALEIN ->
                defaultAnimation = ScaleInAnimation()
            SLIDEIN_LEFT ->
                defaultAnimation = SlideInLeftAnimation()
            SLIDEIN_RIGHT ->
                defaultAnimation = SlideInRightAnimation()
            SLIDEIN_BOTTOM ->
                defaultAnimation = SlideInBottomAnimation()
        }
    }

    fun openLoadAnimation(customAnimation: BaseAnimation) {
        isOpenAnimation = true
        defaultAnimation = customAnimation
    }

    fun closeLoadAnimation() {
        this.isOpenAnimation = false
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

    fun getEmptyLayoutCount(): Int {
        //当有数据时候，e'm't'p
        if (mData?.size ?: 0 != 0)
            return 0

        var count = mEmptyLayout?.childCount ?: 0
        return if (count > 0) 1 else 0
    }

    private fun getHeaderLayoutPosition(): Int {
        return 0
    }

    private fun getFooterLayoutPosition(): Int = if (getEmptyLayoutCount() == 1) {
        getHeaderLayoutPosition() + getEmptyLayoutCount()
    } else {
        (mData?.size ?: 0) + getHeaderLayoutCount()
    }

    private fun getEmptyLayoutPosition(): Int {
        return getHeaderLayoutCount()
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

    private fun addAnimation(holder: BaseViewHolder) {
        if (isOpenAnimation) {
            if (!isFirstOpenOnly || holder.layoutPosition > mLastPositioin) {
                mLastPositioin = holder.layoutPosition
                for (animation in defaultAnimation.getAnimators(holder.itemView)) {
                    animation.duration = animDuration
                    animation.interpolator = LinearInterpolator()
                    animation.start()
                }
            }
        }
    }

    private fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW
    }

    private fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    abstract fun convert(viewHolder: BaseViewHolder, item: T)
}

