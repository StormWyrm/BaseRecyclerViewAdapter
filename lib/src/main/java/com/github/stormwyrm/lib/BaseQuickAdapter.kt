package com.github.stormwyrm.lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlin.RuntimeException

abstract class BaseQuickAdapter<T>(
    @LayoutRes val layoutId: Int,
    var mData: List<T>?
) : RecyclerView.Adapter<BaseViewHolder>() {

    lateinit var mLayoutInflater: LayoutInflater
        private set

    var onItemClickListener: ((adapter: BaseQuickAdapter<T>, v: View, position: Int) -> Unit)? = null
    var onItemLongClickListener: ((adapter: BaseQuickAdapter<T>, v: View, position: Int) -> Boolean)? = null
    var onItemChildClickListener: ((adapter: BaseQuickAdapter<T>, v: View, position: Int) -> Unit)? = null
    var onItemChildLongClickListener: ((adapter: BaseQuickAdapter<T>, v: View, position: Int) -> Boolean)? = null

    constructor(layoutId: Int) : this(layoutId, null)

    constructor(data: List<T>?) : this(0, data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mLayoutInflater = LayoutInflater.from(parent.context)
        val itemView = mLayoutInflater.inflate(layoutId, parent,false)
        val baseViewHolder = BaseViewHolder(itemView)
        bindViewClickListener(baseViewHolder)
        return baseViewHolder
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, getItem(position))
    }

    private fun getItem(@IntRange(from = 0) position: Int): T =
        mData?.get(position) ?: throw RuntimeException("the data is empty")


    override fun getItemCount(): Int = mData?.size ?: 0

    fun bindViewClickListener(baseViewHolder: BaseViewHolder) {
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