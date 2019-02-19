package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_basis_setting.*

class AnimationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Animation")
        setBackBtn()
        setContentView(R.layout.activity_basis_setting)
        initData()
    }

    private fun initData() {
        val data = arrayListOf(
            "string 0",
            "string 1",
            "string 2",
            "string 0",
            "string 1",
            "string 2",
            "string 3",
            "string 4",
            "string 5",
            "string 6",
            "string 3",
            "string 4",
            "string 5",
            "string 6",
            "string 0",
            "string 1",
            "string 2",
            "string 0",
            "string 1",
            "string 2",
            "string 3",
            "string 4",
            "string 5",
            "string 6",
            "string 3",
            "string 4",
            "string 5",
            "string 6"
        )
        val adapter = AnimationAdapter(data)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private class AnimationAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.item_main, data) {

        override fun convert(viewHolder: BaseViewHolder, item: String) {
            openLoadAnimation(SLIDEIN_RIGHT)
            viewHolder.setText(R.id.tvTitle, item)

        }
    }
}