package me.jbusdriver.component.image.browser.ui

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import me.jbusdriver.base.common.C
import me.jbusdriver.component.image.browser.ui.activity.WatchLargeImageActivity

class ImageBrowserComponent : IComponent {

    override fun onCall(cc: CC): Boolean {
        when (cc.actionName) {
            C.C_IMAGE_BROWSER.Browser_Images -> {
                val images = cc.getParamItem<List<String>>("images")
                val index = cc.getParamItem("index") ?: 0
                WatchLargeImageActivity.startShow(cc.context, images, index)
                CC.sendCCResult(cc.callId, CCResult.success())
            }
        //确保每个逻辑分支上都会调用CC.sendCCResult将结果发送给调用方
            else -> CC.sendCCResult(cc.callId
                    , CCResult.error("actionName ${cc.actionName} does not support"))
        }
        return false
    }

    override fun getName() = C.C_IMAGE_BROWSER::class.java.name!!
}