package com.github.stormwyrm.baserecyclerviewadapter

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    val title = arrayListOf("Basis Setting", "HeaderAndFooter", "EmptyView","Animation")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("BaseRecyclerViewAdapter")
        setBackListener(null)
        setContentView(R.layout.activity_main)
        val adapter = MainAdapter(title)
        adapter.onItemClickListener = { _, _, position ->
            when (position) {
                0 -> startActivity(Intent(this@MainActivity, BasisSetttingActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity, HeaderAndFooterActivity::class.java))
                2 -> startActivity(Intent(this@MainActivity, EmptyViewActivity::class.java))
                3 -> startActivity(Intent(this@MainActivity, AnimationActivity::class.java))
            }
        }
        rvMain.layoutManager = GridLayoutManager(this, 2)
        rvMain.adapter = adapter
    }

    private class MainAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.item_main, data) {

        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.tvTitle, item)
        }
    }
}
