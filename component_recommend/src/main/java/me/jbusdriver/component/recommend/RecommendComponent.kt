package me.jbusdriver.component.recommend

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import com.google.gson.JsonObject
import io.reactivex.Flowable
import me.jbusdriver.base.*
import me.jbusdriver.base.common.C
import me.jbusdriver.component.recommend.http.RecommendService
import me.jbusdriver.base.mvp.bean.RecommendBean
import me.jbusdriver.component.recommend.mvp.model.RecommendModel
import me.jbusdriver.component.recommend.ui.activity.HotRecommendActivity

class RecommendComponent : IComponent {


    override fun onCall(cc: CC): Boolean {
        KLog.d("on call ${cc.callId} ${cc.actionName} $cc")
        when (cc.actionName) {
            C.C_RECOMMEND.Open_Recommend -> {
                cc.openActivity<HotRecommendActivity>()
            }
            C.C_RECOMMEND.Recommend_Like_Count -> {
                val likeKey = cc.getParamItem<String?>("key")
                if (likeKey == null) {
                    CC.sendCCResult(cc.callId, CCResult.error("like key must not null"))
                    return false
                }

                CC.sendCCResult(cc.callId , CCResult.success("recommend_count", RecommendModel.getLikeCount(likeKey)))
                return true
            }

            C.C_RECOMMEND.Recommend_Like_It -> {
                val likeKey = cc.getParamItem<String?>("key")
                val reason = cc.getParamItem<String?>("reason")
                val recommendBean = cc.getParamItem<RecommendBean?>("bean")
                val ctx = cc.context
                KLog.d("cc action :${cc.actionName} , params :$likeKey , $reason  , $recommendBean")
                if (likeKey == null) {
                    CC.sendCCResult(cc.callId, CCResult.error("like key must not null"))
                    return false
                }
                if (recommendBean == null) {
                    CC.sendCCResult(cc.callId, CCResult.error("recommendBean  must not null"))
                    return false
                }

                //like
                Flowable.fromCallable {
                    RecommendModel.getLikeCount(likeKey)
                }.flatMap { c ->
                    if (c > 3) {
                        error("一天点赞最多3次")
                    }

                    //get unique id
                    val uid = RecommendModel.getLikeUID(likeKey)

                    val opt = if (BuildConfig.DEBUG) {
                        Flowable.just(JsonObject())
                    } else {
                        val params = arrayMapof(
                                "uid" to uid,
                                "key" to recommendBean.toJsonString()
                        )
                        if (reason.orEmpty().isNotBlank()) {
                            params.put("reason", reason)
                        }
                        RecommendService.INSTANCE.putRecommends(params)
                    }

                    opt.map {
                        KLog.d("res : $it")
                        RecommendModel.save(likeKey, uid)
                        it["message"]?.asString?.let {
                            ctx.toast(it)
                        }
                        return@map Math.min(c + 1, 3)
                    }
                }.onErrorReturn {
                    it.message?.let {
                        ctx.toast(it)
                    }
                    3
                }.addUserCase().takeUntil { cc.isStopped }.compose(SchedulersCompat.io()).subscribeWith(object : SimpleSubscriber<Int>() {
                    override fun onNext(t: Int) {
                        super.onNext(t)
                        CC.sendCCResult(cc.callId, CCResult.success("recommend_count", t))
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        CC.sendCCResult(cc.callId, CCResult.error(e.message))
                    }
                })

                return true

            }

        //确保每个逻辑分支上都会调用CC.sendCCResult将结果发送给调用方
            else -> CC.sendCCResult(cc.callId
                    , CCResult.error("actionName ${cc.actionName} does not support"))
        }
        return false

    }

    override fun getName() = C.C_RECOMMEND::class.java.name!!

}