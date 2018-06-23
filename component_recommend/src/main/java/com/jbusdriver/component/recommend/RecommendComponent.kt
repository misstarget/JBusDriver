package com.jbusdriver.component.recommend

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import com.jbusdriver.component.recommend.ui.activity.HotRecommendActivity
import me.jbusdriver.base.KLog
import me.jbusdriver.base.common.C
import me.jbusdriver.base.openActivity

class RecommendComponent : IComponent {


    override fun onCall(cc: CC): Boolean {
        KLog.d("on call ${cc.callId} ${cc.actionName}")
        when (cc.actionName) {
            C.C_RECOMMEND.OPEN_RECOMMEND -> {
                KLog.d("cc action :${C.C_RECOMMEND.OPEN_RECOMMEND}")
                cc.openActivity<HotRecommendActivity>()
            }
        //确保每个逻辑分支上都会调用CC.sendCCResult将结果发送给调用方
            else -> CC.sendCCResult(cc.callId
                    , CCResult.error("actionName ${cc.actionName} does not support"))
        }
        return false

    }

    override fun getName() = C.C_RECOMMEND::class.java.name

}