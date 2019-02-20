package com.github.stormwyrm.baserecyclerviewadapter

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MultiActivity : BaseActivity() {
    val title = mutableListOf("MULTI ITEM","MULTI ITEM RV")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Multi Item")
        setBackListener(null)
        setContentView(R.layout.activity_main)
        val adapter = MainAdapter(title)
        adapter.onItemClickListener = { _, _, position ->
            when (position) {
                0 -> startActivity(Intent(this@MultiActivity, MultiItemActivity::class.java))
                1 -> startActivity(Intent(this@MultiActivity, MultiItemRvActivity::class.java))
            }
        }
        rvMain.layoutManager = GridLayoutManager(this, 2)
        rvMain.adapter = adapter
    }

    private class MainAdapter(data: MutableList<String>) : BaseQuickAdapter<String>(R.layout.item_main, data) {

        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.tvTitle, item)
        }
    }
}
