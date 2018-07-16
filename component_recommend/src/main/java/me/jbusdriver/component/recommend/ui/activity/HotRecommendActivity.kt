package me.jbusdriver.component.recommend.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.jbusdriver.component.recommend.ui.fragment.RecommendListFragment
import kotlinx.android.synthetic.main.recommend_activity_hot_recommend.*
import me.jbusdriver.base.common.BaseActivity
import me.jbusdriver.component.recommend.R

class HotRecommendActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommend_activity_hot_recommend)
        setSupportActionBar(recommend_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "热门推荐"

        supportFragmentManager.beginTransaction().replace(R.id.fl_hot_recommend, RecommendListFragment.newInstance()).commit()
    }


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HotRecommendActivity::class.java))
        }
    }
}
