package me.jbusdriver.component.movie.detail

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import me.jbusdriver.base.common.C
import me.jbusdriver.base.mvp.bean.Movie
import me.jbusdriver.component.movie.detail.ui.activity.MovieDetailActivity

class MovieDetailComponent : IComponent {
    override fun onCall(cc: CC): Boolean {
        when (cc.actionName) {
            C.C_MOVIE_DETAIL.Open_Movie_Detail -> {
                val isInternal = cc.params.containsKey("movie_bean")
                if (isInternal) {
                    val bean = cc.getParamItem<Movie?>("movie_bean")
                    if (bean == null) {
                        CC.sendCCResult(cc.callId, CCResult.error("movie bean must not be null"))
                        return false
                    }
                    val isFromHistory = cc.getParamItem("from_history") ?: false
                    MovieDetailActivity.start(cc.context, bean, isFromHistory)
                } else {
                    val url = cc.getParamItem<String>("movie_url")
                    if (url.isNullOrEmpty()) {
                        CC.sendCCResult(cc.callId, CCResult.error("movie url is empty"))
                        return false
                    }
                    MovieDetailActivity.start(cc.context, url)
                }
                CC.sendCCResult(cc.callId, CCResult.success())
            }
        }
        return false
    }

    override fun getName() = C.C_MOVIE_DETAIL::class.java.name!!
}