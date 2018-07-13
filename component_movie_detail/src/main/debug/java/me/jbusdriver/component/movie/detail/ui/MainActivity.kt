package me.jbusdriver.component.movie.detail.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.jbusdriver.component.movie.detail.R
import me.jbusdriver.component.movie.detail.ui.activity.WatchLargeImageActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_watch_large_images.setOnClickListener {
            startActivity(Intent(this, WatchLargeImageActivity::class.java))
        }
    }
}
