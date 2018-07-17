package me.jbusdriver.component.recommend.ui.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.billy.cc.core.component.CC
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import me.jbusdriver.base.KLog
import me.jbusdriver.base.common.AppBaseRecycleFragment
import me.jbusdriver.base.common.C
import me.jbusdriver.base.common.toGlideUrl
import me.jbusdriver.base.http.JAVBusService.Companion.defaultFastUrl
import me.jbusdriver.base.urlHost
import me.jbusdriver.base.urlPath
import me.jbusdriver.component.recommend.R
import me.jbusdriver.base.mvp.bean.RecommendRespBean
import me.jbusdriver.component.recommend.mvp.presenter.HotRecommendPresenterImpl
import me.jbusdriver.component.recommend.ui.contract.Contract.HotRecommendContract
import me.jbusdriver.component.recommend.ui.contract.Contract.HotRecommendContract.HotRecommendView


/**
 * Created by Administrator on 2017/7/30.
 */
class RecommendListFragment : AppBaseRecycleFragment<HotRecommendContract.HotRecommendPresenter, HotRecommendView, RecommendRespBean>(), HotRecommendView {

    override fun createPresenter() = HotRecommendPresenterImpl()

    override val layoutId: Int = R.layout.basic_layout_swipe_recycle
    override val swipeView: SwipeRefreshLayout? get() = findView(R.id.basic_sr_refresh)
    override val recycleView: RecyclerView
        get() = findView(R.id.basic_rv_recycle)
                ?: error("not find RecyclerView")
    override val layoutManager: RecyclerView.LayoutManager  by lazy { LinearLayoutManager(viewContext) }

    override val adapter = object : BaseQuickAdapter<RecommendRespBean, BaseViewHolder>(R.layout.recommend_layout_recommend_item) {

        override fun convert(helper: BaseViewHolder, item: RecommendRespBean) {
            Glide.with(viewContext).load(item.key.img.toGlideUrl).into(helper.getView(R.id.recommend_iv_img))
            helper.setText(R.id.recommend_tv_title, item.key.name)
                    .setText(R.id.recommend_tv_reason, if (item.reason?.isNotBlank() == true) "推荐理由：${item.reason}" else "")
                    .setText(R.id.recommend_tv_score, item.score.toString())


        }

    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        adapter.setOnLoadMoreListener({
            KLog.d("onLoadMore")
            mBasePresenter?.onLoadMore()

        }, recycleView)
        adapter.setLoadMoreView(object : LoadMoreView() {
            override fun getLayoutId(): Int = R.layout.recommend_layout_load_reset

            override fun getLoadingViewId(): Int = R.id.recommend_tv_end

            override fun getLoadEndViewId(): Int = R.id.recommend_tv_end

            override fun getLoadFailViewId(): Int = R.id.recommend_tv_end
        })
        adapter.setOnItemClickListener { _, view, position ->
            adapter.getItem(position)?.let {
                KLog.d(it.key)
//                val image = defaultImageUrlHosts[if (it.key.img.endsWith("xyz")) "xyz" else "default"]?.map { h -> h + it.key.img } ?: emptyList()
                val xyz = it.key.url.urlHost.endsWith("xyz")
                val needChange = !xyz && it.key.url.urlHost != defaultFastUrl
                val url = if (needChange) defaultFastUrl + it.key.url.urlPath else it.key.url

                if (it.key.url.contains("/star/", false)) {
                    //todo add link
//                    MovieListActivity.start(viewContext, ActressInfo(it.key.name, it.key.img, url))
                } else {
//                    SearchResultActivity.start(this.viewContext, it.key.name.split(" ").component1())
                    CC.obtainBuilder(C.C_MOVIE_DETAIL::class.java.name)
                            .setActionName(C.C_MOVIE_DETAIL.Open_Movie_Detail)
                            .setContext(viewContext)
                            .addParam("movie_url", url)
                            .setTimeout(3000)
                            .build()
                            .call()
                }
            }
        }
    }

    override fun showContents(data: List<*>) {
        val size = adapter.data.size
        adapter.data.clear()
        adapter.notifyItemRangeRemoved(0, size)
        super.showContents(data)
    }

    override fun loadMoreEnd(clickable: Boolean) {
        super.loadMoreEnd(true)
    }

    companion object {
        fun newInstance() = RecommendListFragment()

    }

}