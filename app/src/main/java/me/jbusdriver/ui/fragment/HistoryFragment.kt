package me.jbusdriver.ui.fragment

import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import jbusdriver.me.jbusdriver.R
import me.jbusdriver.base.GlideApp
import me.jbusdriver.base.common.AppBaseRecycleFragment
import me.jbusdriver.base.common.toGlideUrl
import me.jbusdriver.base.mvp.bean.*
import me.jbusdriver.mvp.HistoryContract
import me.jbusdriver.mvp.presenter.HistoryPresenterImpl
import me.jbusdriver.base.mvp.ui.adapter.BaseAppAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2017/9/18 0018.
 */
class HistoryFragment : AppBaseRecycleFragment<HistoryContract.HistoryPresenter, HistoryContract.HistoryView, History>(), HistoryContract.HistoryView {

    override fun createPresenter() = HistoryPresenterImpl()

    override val layoutId: Int = R.layout.basic_layout_swipe_recycle
    override val swipeView: SwipeRefreshLayout? get() = findView(R.id.basic_sr_refresh)
    override val recycleView: RecyclerView get() = findView(R.id.basic_rv_recycle)
    override val layoutManager: RecyclerView.LayoutManager  by lazy { LinearLayoutManager(viewContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(Menu.NONE, R.id.cancel_action, 10, "清除历史记录").apply {
            setIcon(R.drawable.ic_delete_24dp)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setOnMenuItemClickListener {
                mBasePresenter?.clearHistory()
                adapter.setNewData(null)
                adapter.notifyDataSetChanged()
                true
            }

        }

    }

    override val adapter: BaseQuickAdapter<History, in BaseViewHolder> by lazy {


        object : BaseAppAdapter<History, BaseViewHolder>(R.layout.layout_history_item) {

            val linkCache by lazy { ArrayMap<Int, ILink>() }
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

            override fun convert(helper: BaseViewHolder, item: History) {
                val itemLink = linkCache.getOrPut(item.hashCode()) { item.getLinkItem() }
                val appender = if (itemLink !is Movie && itemLink !is SearchLink) {
                    if (item.isAll) "全部电影" else "已有种子电影"
                } else ""
                helper.setText(R.id.tv_history_date, format.format(item.createTime))
                        .setText(R.id.tv_history_title, itemLink.des + appender)

                val img by lazy {
                    (itemLink as? ActressInfo)?.avatar ?: (itemLink as? Movie)?.imageUrl ?: ""
                }

                if (img.isNotBlank()) {
                    helper.setVisible(R.id.iv_history_icon, true)
                    GlideApp.with(mContext).load(img.toGlideUrl).into(helper.getView(R.id.iv_history_icon))
                } else {
                    helper.setGone(R.id.iv_history_icon, false)
                }
            }

        }.apply {
            setOnItemClickListener { _, view, position ->
                /**
                 *   when (type) {
                1 -> MovieDetailActivity.start(context, getLinkItem() as Movie, true)
                in 2..6 -> MovieListActivity.reloadFromHistory(context, this)
                else -> JBusManager.manager.firstOrNull()?.get()?.toast("没有可以跳转的界面")
                }


                todo
                 */
                //data.getOrNull(position)?.move(view.context)
            }
        }

    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

}