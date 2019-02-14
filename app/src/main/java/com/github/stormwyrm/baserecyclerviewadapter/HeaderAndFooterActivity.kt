package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_header_and_footer.*

class HeaderAndFooterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("HeaderAndFooter")
        setBackBtn()
        setContentView(R.layout.activity_header_and_footer)
        initAdapter()
    }

    private fun initAdapter() {
        val data = arrayListOf("DATA 0", "DATA 1", "DATA 2")
        val adapter = HeaderAndFooterAdapter(data)
        adapter.addHeaderView(getHeaderView(0, View.OnClickListener {
            adapter.addHeaderView(getHeaderView(1, View.OnClickListener { view ->
                adapter.removeHeaderView(view)
            }))
        }))
        adapter.addFooterView(getFooterView(0, View.OnClickListener {
            adapter.addFooterView(getFooterView(1, View.OnClickListener { view ->
                adapter.removeFooterView(view)
            }))
        }))
        rvHeaderFooter.layoutManager = LinearLayoutManager(this)
        rvHeaderFooter.adapter = adapter
    }

    private fun getHeaderView(type: Int, listener: View.OnClickListener): View {
        val view = View.inflate(this, R.layout.layout_header, null)
        if (type == 1) {
            view.findViewById<TextView>(R.id.tvTitle).text = "- HeaderView"
        }
        view.setOnClickListener(listener)
        return view
    }

    private fun getFooterView(type: Int, listener: View.OnClickListener): View {
        val view = View.inflate(this, R.layout.layout_footer, null)
        if (type == 1) {
            view.findViewById<TextView>(R.id.tvTitle).text = "- FooterView"
        }
        view.setOnClickListener(listener)
        return view
    }


    class HeaderAndFooterAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.item_main,data) {
        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.tvTitle,item)
        }
    }
}
