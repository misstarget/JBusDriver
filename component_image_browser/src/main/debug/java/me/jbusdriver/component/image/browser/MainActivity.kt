package me.jbusdriver.component.image.browser

import android.app.Activity
import android.os.Bundle
import com.billy.cc.core.component.CC
import kotlinx.android.synthetic.main.activity_main.*
import me.jbusdriver.base.common.C

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        browser_images_btn.setOnClickListener {
            val cc = CC.obtainBuilder(C.C_IMAGE_BROWSER::class.java.name)
                    .setActionName(C.C_IMAGE_BROWSER.Browser_Images)
                    .setParams(mapOf(
                            "images" to listOf("http://e.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fd0e9be27fc1f3a292cf57842.jpg",
                                    "http://a.hiphotos.baidu.com/image/pic/item/09fa513d269759eee5b61ac2befb43166c22dfd1.jpg",
                                    "http://g.hiphotos.baidu.com/image/pic/item/96dda144ad345982a4099c9b00f431adcbef8433.jpg"
                            )
                    ))
                    .build()

            val res = cc.call()

            browser_images_info.text = "$cc \r\n $res"


        }
    }
}
