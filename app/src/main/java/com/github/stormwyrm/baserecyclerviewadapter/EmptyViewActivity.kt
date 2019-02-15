package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_empty_view.*

class EmptyViewActivity : BaseActivity() {
    private var mError = true
    private var mNoData = true

    private val adapter: EmptyViewAdapter by lazy {
        EmptyViewAdapter(arrayListOf())
    }

    private val noDataView: View by lazy {
        View.inflate(this, R.layout.layout_empty_view, null)
    }
    private val errorView: View by lazy {
        View.inflate(this, R.layout.layout_error_view,null)
    }
    private val loadingView: View by lazy {
        LayoutInflater.from(rvEmptyView.context).inflate(R.layout.layout_loading_view, rvEmptyView, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("EmptyView Use")
        setContentView(R.layout.activity_empty_view)
        noDataView.setOnClickListener {
            freshData()
        }
        errorView.setOnClickListener {
            freshData()
        }
        rvEmptyView.layoutManager = LinearLayoutManager(this)
        rvEmptyView.adapter = adapter
        freshData()
    }

    private fun freshData() {
        adapter.setEmptyView(loadingView)
        Handler().postDelayed({
            when {
                mError -> {
                    adapter.setEmptyView(errorView)
                    mError = false
                }
                mNoData -> {
                    adapter.setEmptyView(noDataView)
                    mNoData = false
                }
                else -> adapter.setNewData(
                    arrayListOf(
                        "data0",
                        "data0",
                        "data0",
                        "data0",
                        "data0",
                        "data0",
                        "data0",
                        "data0"
                    )
                )
            }
        }, 3000)
    }

    class EmptyViewAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.item_main, data) {
        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.tvTitle, item)
        }

    }

}
