package com.github.stormwyrm.lib

import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.github.stormwyrm.lib.entity.MultiItemEntity

abstract class BaseMultiItemQuickAdapter<T : MultiItemEntity>(
    mData: List<T>?
) : BaseQuickAdapter<T>(mData) {
    val layouts: SparseArray<Int> = SparseArray()

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createBaseViewHolder(parent, getLayoutId(viewType))
    }

    override fun getDefItemViewType(data: List<T>?, position: Int): Int {
        return data?.get(position)?.getItemType() ?: DEFAULT_VIEW
    }

    fun addItemType(viewType: Int, @LayoutRes layoutId: Int) {
        layouts.put(viewType, layoutId)
    }

    fun getLayoutId(viewType: Int): Int {
        return layouts.get(viewType)
    }
}