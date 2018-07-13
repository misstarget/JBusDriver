package me.jbusdriver.component.movie.detail.ui.holder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.layout_detail_actress.view.*
import me.jbusdriver.base.KLog
import me.jbusdriver.base.inflate
import me.jbusdriver.base.mvp.bean.ActressInfo
import me.jbusdriver.base.mvp.ui.holder.BaseHolder
import me.jbusdriver.component.movie.detail.R
import me.jbusdriver.component.movie.detail.ui.adapter.ActressInfoAdapter

/**
 * Created by Administrator on 2017/5/9 0009.
 */
class ActressListHolder(context: Context) : BaseHolder(context) {

    val view by lazy {
        weakRef.get()?.let {
            it.inflate(R.layout.layout_detail_actress).apply {
                rv_recycle_actress.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                actressAdapter.bindToRecyclerView(rv_recycle_actress)
                rv_recycle_actress.isNestedScrollingEnabled = true
                actressAdapter.setOnItemClickListener { _, _, position ->
                    actressAdapter.data.getOrNull(position)?.let { item ->
                        KLog.d("item : $it")
                        weakRef.get()?.let {
                            //                            todo
//                            MovieListActivity.start(it, item)
                        }
                    }
                }
            }
        } ?: error("context ref is finish")
    }

    private val actressAdapter by lazy {
        ActressInfoAdapter(rxManager)
    }


    fun init(actress: List<ActressInfo>) {
        //actress
        if (actress.isEmpty()) view.tv_movie_actress_none_tip.visibility = View.VISIBLE
        else {
            //load header
            actressAdapter.setNewData(actress)
        }
    }

}