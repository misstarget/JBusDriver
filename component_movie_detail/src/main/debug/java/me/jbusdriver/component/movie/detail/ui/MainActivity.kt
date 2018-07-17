package me.jbusdriver.component.movie.detail.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.billy.cc.core.component.CC
import kotlinx.android.synthetic.main.activity_main.*
import me.jbusdriver.base.common.C
import me.jbusdriver.base.mvp.bean.Movie
import me.jbusdriver.component.movie.detail.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        detail_from_bean.setOnClickListener {
            CC.obtainBuilder(C.C_MOVIE_DETAIL::class.java.name)
                    .setActionName(C.C_MOVIE_DETAIL.Open_Movie_Detail)
                    .addParam("movie_bean", Movie("MIAE-236", "https://pics.javcdn.pw/thumb/6jra.jpg", "MIAE-236", "", "https://www.javbus6.pw/MIAE-236"))
                    .addParam("from_history", Math.random() <= 0.5)
                    .build()
                    .call()
        }

        detail_from_url.setOnClickListener {
            CC.obtainBuilder(C.C_MOVIE_DETAIL::class.java.name)
                    .setActionName(C.C_MOVIE_DETAIL.Open_Movie_Detail)
                    .addParam("movie_url", "https://www.javbus6.pw/MIAE-236")
                    .build()
                    .call()
        }
    }
}
