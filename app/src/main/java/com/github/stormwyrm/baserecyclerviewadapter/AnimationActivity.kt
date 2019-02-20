package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.stormwyrm.lib.BaseQuickAdapter
import com.github.stormwyrm.lib.BaseViewHolder
import kotlinx.android.synthetic.main.activity_basis_setting.*

class AnimationActivity : BaseActivity() {
    lateinit var data: List<String>
    private val adapter: BaseQuickAdapter<String> by lazy {
        AnimationAdapter(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Animation")
        setBackBtn()
        setContentView(R.layout.activity_basis_setting)
        initData()
    }

    private fun initData() {
        data = arrayListOf(
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
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_animation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item -> {
                if (adapter.isOpenAnimation) {
                    adapter.openLoadAnimation()
                    item.title = "关闭动画"
                } else {
                    adapter.closeLoadAnimation()
                    item.title = "开启动画"
                }

            }
            R.id.item1 -> {
                if (adapter.isFirstOpenOnly) {
                    adapter.isFirstOpenOnly = false
                    item.title = "首次加载"
                }else{
                    adapter.isFirstOpenOnly = false
                    item.title = "每次加载"
                }
            }
            R.id.item2 -> {
                adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
            }
            R.id.item3 -> {
                adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
            }
            R.id.item4 -> {
                adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            }
            R.id.item5 -> {
                adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
            }
            R.id.item6 -> {
                adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            }
        }
        adapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    private class AnimationAdapter(data: List<String>) : BaseQuickAdapter<String>(R.layout.item_main, data) {
        init {
            openLoadAnimation(SLIDEIN_RIGHT)
        }
        override fun convert(viewHolder: BaseViewHolder, item: String) {
            viewHolder.setText(R.id.tvTitle, item)
        }
    }
}