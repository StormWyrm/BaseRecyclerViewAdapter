package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            "string 6"
        )
        val adapter = MainAdapter(data)
        adapter.onItemLongClickListener = { helper, view, position ->
            Toast.makeText(this, "长按位置: $position", Toast.LENGTH_SHORT).show()
            true
        }
        adapter.onItemClickListener = { helper, view, position ->
            Toast.makeText(this, "点击位置: $position", Toast.LENGTH_SHORT).show()
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private class MainAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.recycler_view_item, data) {

        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.textView1, item)
        }
    }
}
