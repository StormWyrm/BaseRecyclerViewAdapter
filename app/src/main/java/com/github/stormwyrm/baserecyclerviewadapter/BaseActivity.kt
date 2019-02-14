package com.github.stormwyrm.baserecyclerviewadapter

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*

open class BaseActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(View.inflate(this, layoutResID, null))
    }

    override fun setContentView(view: View?) {
        val rootLayout = findViewById<LinearLayout>(R.id.root_layout)
        rootLayout.addView(view)
        initToolbar()
    }

    fun setTitle(msg: String) {
        tvTitle.text = msg
    }

    /**
     * sometime you want to define back event
     */
    fun setBackBtn() {
        ivBack?.run {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
    }

    fun setBackListener(callback: ((View) -> Unit)?) {
        ivBack?.apply {
            when (callback) {
                null ->
                    visibility = View.GONE
                else -> {
                    visibility = View.VISIBLE
                    setOnClickListener(callback)
                }
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

    }
}