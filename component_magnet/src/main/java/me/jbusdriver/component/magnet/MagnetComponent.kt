package me.jbusdriver.component.magnet

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import me.jbusdriver.base.common.C
import me.jbusdriver.component.magnet.ui.activity.MagnetPagerListActivity

class MagnetComponent : IComponent {
    override fun onCall(cc: CC): Boolean {
        when (cc.actionName) {
            C.C_MAGNET.Open_Search_Magnet_Result -> {
                val keyword = cc.getParamItem<CharSequence?>("keyword")
                if (keyword.isNullOrBlank()) {
                    CC.sendCCResult(cc.callId, CCResult.error("搜索关键字不能为空！"))
                    return false
                }

                MagnetPagerListActivity.start(cc.context, keyword.toString())
                CC.sendCCResult(cc.callId, CCResult.success())
            }
        }

        return false
    }

    override fun getName() = C.C_MAGNET::class.java.name!!
}