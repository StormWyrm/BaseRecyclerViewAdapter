package com.github.stormwyrm.lib

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val views: SparseArray<View> = SparseArray()//用于保存itemView中的子View
    val childClickViewIds: LinkedHashSet<Int> = LinkedHashSet()//保存child点击事件的view id
    val childLongClickViewIds: LinkedHashSet<Int> = LinkedHashSet()////保存child长点击事件的view id

    var adapter: BaseQuickAdapter<*>? = null


    fun addChildClickId(@IdRes vararg ids: Int): BaseViewHolder {
        for (id in ids) {
            childClickViewIds.add(id)
            val view = getView<View>(id)
            view.setOnClickListener { view ->
                adapter?.let {
                    it.onItemChildClickListener?.invoke(it, view, layoutPosition)
                }
            }
        }
        return this
    }

    fun addChildLongClickId(@IdRes vararg ids: Int): BaseViewHolder {
        for (id in ids) {
            childLongClickViewIds.add(id)
            val view = getView<View>(id)
            view.setOnLongClickListener { view ->
                adapter?.let {
                    return@setOnLongClickListener it.onItemChildLongClickListener?.invoke(it, view, layoutPosition)
                        ?: false
                } ?: false
            }
        }
        return this
    }

    fun setText(@IdRes viewId: Int, text: CharSequence): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes stringId: Int): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.setText(stringId)
        return this
    }

    fun <T : View> getView(@IdRes viewId: Int): T {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
            views.put(viewId, view)
        }
        return view as T
    }
}