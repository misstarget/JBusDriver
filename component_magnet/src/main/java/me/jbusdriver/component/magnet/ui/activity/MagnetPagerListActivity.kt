package me.jbusdriver.component.magnet.ui.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.magnet_activity_magnet_list.*
import me.jbusdriver.base.common.BaseActivity
import me.jbusdriver.base.common.C
import me.jbusdriver.component.magnet.R
import me.jbusdriver.component.magnet.ui.fragment.MagnetPagersFragment

class MagnetPagerListActivity : BaseActivity() {

    private val keyword by lazy {
        intent.getStringExtra(C.BundleKey.Key_1) ?: error("must set keyword")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.magnet_activity_magnet_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(keyword)
        //go to SearchResultPagesFragment
        supportFragmentManager.beginTransaction().replace(R.id.fl_magnet_list, MagnetPagersFragment().apply {
            arguments = Bundle().apply { putString(C.BundleKey.Key_1, keyword) }
        }).commit()

    }

    private fun setTitle(title: String) {
        supportActionBar?.title = "$title 的磁力链接"
    }

    companion object {
        fun start(context: Context, keyword: String) {
            context.startActivity(Intent(context, MagnetPagerListActivity::class.java).apply {
                if (context is Application) {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                putExtra(C.BundleKey.Key_1, keyword)
            })
        }
    }
}
