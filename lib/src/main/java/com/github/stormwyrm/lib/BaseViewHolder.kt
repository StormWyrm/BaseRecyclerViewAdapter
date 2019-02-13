package com.github.stormwyrm.lib

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val views: SparseArray<View> = SparseArray()

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