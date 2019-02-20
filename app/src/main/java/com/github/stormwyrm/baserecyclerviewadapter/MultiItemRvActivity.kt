package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.baserecyclerviewadapter.bean.MultiBean
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import com.github.stormwyrm.lib.multi.MultiTypeDelegate
import kotlinx.android.synthetic.main.activity_basis_setting.*

class MultiItemRvActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Multi Rv Item")
        setBackBtn()
        setContentView(R.layout.activity_basis_setting)
        initData()
    }

    private fun initData() {
        val datas = ArrayList<MultiBean>()
        for (i in 0..10) {
            datas.add(MultiBean(i, "String $i"))
        }
        val adpter = MultiItemAdapter(datas)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adpter
    }

    class MultiItemAdapter(data: List<MultiBean>) : BaseQuickAdapter<MultiBean>(data) {
        companion object {
            const val MULTI_BEAN_FIRST = 1
            const val MULTI_BEAN_SECOND = 2
        }

        init {
            multiTypeDelegate = object : MultiTypeDelegate<MultiBean>() {
                override fun getItemType(t: MultiBean): Int {
                    return t.getItemType()
                }
            }
            multiTypeDelegate?.run {
                addItemType(MultiItemActivity.MultiItemAdapter.MULTI_BEAN_FIRST, R.layout.item_main)
                addItemType(MultiItemActivity.MultiItemAdapter.MULTI_BEAN_SECOND, R.layout.item_basis_settting)
            }
        }

        override fun convert(helper: BaseViewHolder, item: MultiBean) {
            when (helper.itemViewType) {
                MULTI_BEAN_FIRST -> {
                    helper.setText(R.id.tvTitle, item.name)
                }
                MULTI_BEAN_SECOND -> {
                    helper.setText(R.id.textView1, item.name)
                }
            }
        }
    }
}