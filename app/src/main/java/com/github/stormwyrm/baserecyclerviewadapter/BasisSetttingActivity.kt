package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_basis_setting.*

class BasisSetttingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Basis Setting")
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
            "string 6"
        )
        val adapter = BasisSettingAdapter(data)
        adapter.onItemLongClickListener = { _, _, position ->
            Toast.makeText(this, "长按位置: $position", Toast.LENGTH_SHORT).show()
            true
        }
        adapter.onItemClickListener = { _, _, position ->
            Toast.makeText(this, "点击位置: $position", Toast.LENGTH_SHORT).show()
        }
        adapter.onItemChildClickListener = { _, view, position ->
            when (view.id) {
                R.id.iv -> Toast.makeText(this, "点击图片: $position", Toast.LENGTH_SHORT).show()
                R.id.ll -> Toast.makeText(this, "点击文字: $position", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.onItemChildLongClickListener = { helper, view, position ->
            when (view.id) {
                R.id.iv -> Toast.makeText(this, "长点击图片: $position", Toast.LENGTH_SHORT).show()
                R.id.ll -> Toast.makeText(this, "长点击文字: $position", Toast.LENGTH_SHORT).show()
            }
            true
        }
        adapter.onItemChildClickListener
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private class BasisSettingAdapter(data: MutableList<String>) : BaseQuickAdapter<String>(R.layout.item_basis_settting, data) {

        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.textView1, item)
                .addChildClickId(R.id.iv, R.id.ll)
                .addChildLongClickId(R.id.iv, R.id.ll)

        }
    }
}
