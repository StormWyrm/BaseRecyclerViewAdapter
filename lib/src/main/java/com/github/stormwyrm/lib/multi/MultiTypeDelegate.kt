package com.github.stormwyrm.lib.multi

import android.util.SparseArray
import androidx.annotation.LayoutRes

/**
 * 多布局
 */
abstract class MultiTypeDelegate<T> {
    private val DEFAULT_VIEW_TYPE = -0xff

    val layouts: SparseArray<Int> = SparseArray()

    fun getDefItemViewType(data: List<T>?, position: Int): Int {
        return data?.get(position)?.run {
            getItemType(this)
        } ?: DEFAULT_VIEW_TYPE
    }

    /**
     * 根据itemBean获取type
     */
    abstract fun getItemType(t: T): Int

    /**
     * 根据viewType获取对应的布局layout
     */
    fun getLayoutId(viewType: Int): Int? {
        return layouts.get(viewType)
    }

    /**
     * 添加类型对应的布局
     */
    fun addItemType(type: Int, @LayoutRes layoutId: Int) {
        layouts.put(type, layoutId)
    }

}