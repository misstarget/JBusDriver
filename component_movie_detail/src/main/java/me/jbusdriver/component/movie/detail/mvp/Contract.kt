package me.jbusdriver.component.movie.detail.mvp

import me.jbusdriver.base.mvp.BaseView
import me.jbusdriver.base.mvp.bean.Movie
import me.jbusdriver.base.mvp.presenter.BasePresenter

interface Contract {
    interface MovieDetailView : BaseView {
        val movie: Movie?
        val url: String?
        fun changeLikeIcon(likeCount:Int)
//        fun addMagnet(t: List<Magnet>)
//        fun initMagnetLoad()
    }

    interface MovieDetailPresenter : BasePresenter<MovieDetailView>, BasePresenter.RefreshPresenter {
        fun loadDetail(url:String)
        fun likeIt(movie:Movie,reason:String? = null)
    }
}