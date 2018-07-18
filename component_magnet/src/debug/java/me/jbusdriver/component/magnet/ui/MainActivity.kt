package me.jbusdriver.component.magnet.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.billy.cc.core.component.CC
import me.jbusdriver.base.KLog
import me.jbusdriver.base.common.C
import me.jbusdriver.component.magnet.R

class MainActivity : AppCompatActivity() {

    val search by lazy { findViewById<View>(R.id.magnet_search) }
    val keyword by lazy { findViewById<TextView>(R.id.magnet_keyword) }
    val info by lazy { findViewById<TextView>(R.id.magnet_tv_info) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search.setOnClickListener {
            KLog.d("search ${keyword.text}")
            CC.obtainBuilder(C.C_MAGNET::class.java.name)
                    .setActionName(C.C_MAGNET.Open_Search_Magnet_Result)
                    .addParam("keyword", keyword.text.toString())
                    .build().callAsync { cc, result ->
                        info.text = "$cc \r\n\r\n  $result"
                    }

        }
    }
}
