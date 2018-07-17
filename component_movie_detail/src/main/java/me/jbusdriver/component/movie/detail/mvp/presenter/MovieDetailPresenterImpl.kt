package me.jbusdriver.component.movie.detail.mvp.presenter

import com.billy.cc.core.component.CC
import com.umeng.analytics.pro.cc
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.rxkotlin.addTo
import me.jbusdriver.base.*
import me.jbusdriver.base.common.C
import me.jbusdriver.base.db.service.HistoryService
import me.jbusdriver.base.http.JAVBusService
import me.jbusdriver.base.mvp.bean.*
import me.jbusdriver.base.mvp.model.AbstractBaseModel
import me.jbusdriver.base.mvp.model.BaseModel
import me.jbusdriver.base.mvp.presenter.BasePresenterImpl
import me.jbusdriver.component.movie.detail.mvp.MovieDetailContract
import me.jbusdriver.component.movie.detail.mvp.MovieDetailContract.MovieDetailView
import org.jsoup.Jsoup
import java.util.*

class MovieDetailPresenterImpl(private val fromHistory: Boolean) : BasePresenterImpl<MovieDetailView>(), MovieDetailContract.MovieDetailPresenter {

    private val likeCallIds by lazy { mutableSetOf<String>() }

    private val loadFromNet = { s: String ->
        KLog.d("request for : $s")
        JAVBusService.INSTANCE.get(s).addUserCase().map { MovieDetail.parseDetails(Jsoup.parse(it)) }
                .doOnNext { s.urlPath.let { key -> CacheLoader.cacheDisk(key to it) } }
                ?: Flowable.empty()
    }
    val model: BaseModel<String, MovieDetail> = object : AbstractBaseModel<String, MovieDetail>(loadFromNet) {
        override fun requestFromCache(t: String): Flowable<MovieDetail> {
            val disk = Flowable.create({ emitter: FlowableEmitter<MovieDetail> ->
                mView?.let { view ->
                    val saveKey = t.urlPath
                    CacheLoader.acache.getAsString(saveKey)?.let {
                        val old = GSON.fromJson<MovieDetail>(it)
                        val res = if (old != null && mView?.movie?.link?.endsWith("xyz") == false) {
                            val new = old.checkUrl(JAVBusService.defaultFastUrl)
                            if (old != new) CacheLoader.cacheDisk(saveKey to new)
                            new
                        } else old
                        emitter.onNext(res)
                    } ?: emitter.onComplete()
                } ?: emitter.onComplete()
            }, BackpressureStrategy.DROP)

            return Flowable.concat(disk, requestFor(t)).firstOrError().toFlowable()
        }
    }


    override fun onFirstLoad() {
        super.onFirstLoad()
        val fromUrl = mView?.movie?.link ?: mView?.url ?: error("need url info")
        loadDetail(fromUrl)

        mView?.movie?.let {
            if (!fromHistory)
                HistoryService.insert(History(it.DBtype, Date(), it.toJsonString()))
        }

    }

    override fun onRefresh() {

    }

    override fun loadDetail(url: String) {
        model.requestFromCache(url).compose(SchedulersCompat.io())
                .compose(SchedulersCompat.io())
                .doOnTerminate { mView?.dismissLoading() }
                .subscribeWith(object : SimpleSubscriber<MovieDetail>() {
                    override fun onStart() {
                        super.onStart()
                        mView?.showLoading()
                    }

                    override fun onNext(t: MovieDetail) {
                        super.onNext(t)
                        mView?.showContent(t.generateMovie(url))
                        mView?.showContent(t)
                    }
                })
                .addTo(rxManager)

    }

    fun MovieDetail.generateMovie(url: String): Movie {
        val code = headers.first().value.trim()
        return Movie(title.replace(code, "", true).trim(), this.cover.replace("cover", "thumb").replace("_b", ""),
                code, headers.component2().value, url)
    }

    override fun likeIt(movie: Movie, reason: String?) {
        // key reason bean
        val likeKey = movie.saveKey + "_like"
        val recommendBean = RecommendBean(name = "${movie.code} ${movie.title}", img = movie.imageUrl, url = movie.link)

        likeCallIds.add(
                CC.obtainBuilder(C.C_RECOMMEND::class.java.name)
                        .setActionName(C.C_RECOMMEND.Recommend_Like_It)
                        .setContext(mView?.viewContext)
                        .setParams(mapOf("key" to likeKey,
                                "reason" to reason,
                                "bean" to recommendBean
                        ))
                        .build()
                        .callAsyncCallbackOnMainThread { cc, result ->
                            KLog.d("cc $cc , result $result")
                            if(result.isSuccess){
                                val count = result.getDataItem("recommend_count", -1)
                                mView?.changeLikeIcon(count)
                            }
                        }
        )
    }

    override fun onPresenterDestroyed() {
        super.onPresenterDestroyed()
        likeCallIds.forEach {
            CC.cancel(it)
        }
    }

    override fun restoreFromState() {
        super.restoreFromState()
        mView?.movie?.link?.let {
            loadDetail(it)
        }
    }

}