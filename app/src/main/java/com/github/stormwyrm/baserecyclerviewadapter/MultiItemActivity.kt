package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.baserecyclerviewadapter.bean.MultiBean
import com.github.stormwyrm.lib.BaseMultiItemQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_basis_setting.*

class MultiItemActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Multi Item")
        setBackBtn()
        setContentView(R.layout.activity_basis_setting)
        initData()
    }

    private fun initData() {
        val datas  = ArrayList<MultiBean>()
        for(i in 0..10){
            datas.add(MultiBean(i,"String $i"))
        }
        val adpter = MultiItemAdapter(datas)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adpter
    }

    class MultiItemAdapter(data: List<MultiBean>) : BaseMultiItemQuickAdapter<MultiBean>(data) {
        companion object {
            const val MULTI_BEAN_FIRST = 1
            const val MULTI_BEAN_SECOND = 2
        }

        init {
            addItemType(MULTI_BEAN_FIRST, R.layout.item_main)
            addItemType(MULTI_BEAN_SECOND, R.layout.item_basis_settting)
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