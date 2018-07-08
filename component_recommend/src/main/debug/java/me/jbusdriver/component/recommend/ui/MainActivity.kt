package me.jbusdriver.component.recommend.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.billy.cc.core.component.CC
import kotlinx.android.synthetic.main.activity_main.*
import me.jbusdriver.base.KLog
import me.jbusdriver.base.common.C
import me.jbusdriver.base.postMain
import me.jbusdriver.component.recommend.R
import me.jbusdriver.component.recommend.mvp.bean.RecommendBean

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recommend_btn_show_activity.setOnClickListener {
            CC.obtainBuilder(C.C_RECOMMEND::class.java.name).setContext(this).setActionName(C.C_RECOMMEND.Open_Recommend)
                    .build().callAsync { cc, result ->
                        KLog.d("call $result")
                        postMain {
                            recommend_tv_info.text = cc.callId + "\r\n" + result.toString()
                        }
                    }
        }



        recommend_btn_like_it.setOnClickListener {

            CC.obtainBuilder(C.C_RECOMMEND::class.java.name).setContext(this).setActionName(C.C_RECOMMEND.Recommend_Like_It)
                    .addParam(C.C_RECOMMEND.KEYS.Recommend_Like_Key, "abc")
                    .addParam(C.C_RECOMMEND.KEYS.Recommend_Like_Bean, RecommendBean("hello", "img", "abc"))
                    .build().callAsync { cc, result ->
                        KLog.d("call $result")
                        postMain {
                            recommend_tv_info.text = cc.callId + "\r\n" + result.toString()
                        }
                    }
        }
    }

}
