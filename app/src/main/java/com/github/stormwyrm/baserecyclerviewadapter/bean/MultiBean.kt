package com.github.stormwyrm.baserecyclerviewadapter.bean

import com.github.stormwyrm.baserecyclerviewadapter.MultiItemActivity.MultiItemAdapter.Companion.MULTI_BEAN_FIRST
import com.github.stormwyrm.baserecyclerviewadapter.MultiItemActivity.MultiItemAdapter.Companion.MULTI_BEAN_SECOND
import com.github.stormwyrm.lib.entity.MultiItemEntity

class MultiBean(var position: Int, var name: String) : MultiItemEntity {


    override fun getItemType(): Int =
        if (position % 2 == 0) {
            MULTI_BEAN_FIRST
        } else
            MULTI_BEAN_SECOND
}